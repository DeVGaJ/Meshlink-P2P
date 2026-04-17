package com.devgaj.meshlink.core.network

import android.content.Context
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.nio.charset.StandardCharsets

/**
 * Developed by DeVGaJ - https://github.com/DeVGaJ
 * Core P2P Class for MeshLink.
 */
class MeshNetworkService(private val context: Context, private val userName: String) {

    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(context)
    private val serviceId = "com.devgaj.meshlink.SERVICE_ID"
    private val strategy = Strategy.P2P_CLUSTER

    private val connectedEndpoints = mutableMapOf<String, String>()
    var onMessageReceived: ((String) -> Unit)? = null
    var onSystemMessage: ((String) -> Unit)? = null

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d("MeshNetwork", "Connection initiated with ${info.endpointName} ($endpointId)")
            connectionsClient.acceptConnection(endpointId, payloadCallback)
                .addOnFailureListener { e -> Log.e("MeshNetwork", "Failed to accept connection from $endpointId", e) }
        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when (result.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    connectedEndpoints[endpointId] = endpointId
                    Log.d("MeshNetwork", "CONNECTED to $endpointId")
                    onSystemMessage?.invoke("Connected to a new peer: ${endpointId.take(4)}")
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    onSystemMessage?.invoke("Connection rejected.")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.e("MeshNetwork", "Connection ERROR with $endpointId")
                    onSystemMessage?.invoke("Connection error occurred.")
                }
            }
        }

        override fun onDisconnected(endpointId: String) {
            connectedEndpoints.remove(endpointId)
            Log.d("MeshNetwork", "DISCONNECTED from $endpointId")
        }
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val data = String(payload.asBytes()!!, StandardCharsets.UTF_8)
                Log.d("MeshNetwork", "Received: $data from $endpointId")
                onMessageReceived?.invoke(data)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {}
    }

    fun startMesh() {
        stopMesh()
        startAdvertising()
        startDiscovery()
    }

    fun stopMesh() {
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
        connectionsClient.stopAllEndpoints()
        connectedEndpoints.clear()
    }

    private fun startAdvertising() {
        val options = AdvertisingOptions.Builder().setStrategy(strategy).build()
        connectionsClient.startAdvertising(userName, serviceId, connectionLifecycleCallback, options)
            .addOnSuccessListener { Log.d("MeshNetwork", "Advertising as $userName...") }
            .addOnFailureListener { e -> Log.e("MeshNetwork", "Advertising failed", e) }
    }

    private fun startDiscovery() {
        val options = DiscoveryOptions.Builder().setStrategy(strategy).build()
        connectionsClient.startDiscovery(serviceId, object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                Log.d("MeshNetwork", "Found endpoint: ${info.endpointName} ($endpointId)")
                // To avoid race conditions where both devices request at once, 
                // we can use a simple hash comparison or name comparison.
                if (userName < info.endpointName) {
                    Log.d("MeshNetwork", "Requesting connection to $endpointId")
                    connectionsClient.requestConnection(userName, endpointId, connectionLifecycleCallback)
                        .addOnFailureListener { e -> Log.e("MeshNetwork", "Request connection failed to $endpointId", e) }
                } else {
                    Log.d("MeshNetwork", "Waiting for ${info.endpointName} to request connection...")
                }
            }

            override fun onEndpointLost(endpointId: String) {
                Log.d("MeshNetwork", "Endpoint lost: $endpointId")
            }
        }, options)
    }

    fun sendEncryptedMessage(message: String) {
        if (connectedEndpoints.isEmpty()) {
            Log.w("MeshNetwork", "No connected endpoints to send message to.")
            return
        }
        val payload = Payload.fromBytes(message.toByteArray(StandardCharsets.UTF_8))
        connectedEndpoints.keys.forEach { endpointId ->
            connectionsClient.sendPayload(endpointId, payload)
                .addOnFailureListener { e -> Log.e("MeshNetwork", "Send failed to $endpointId", e) }
        }
    }
}
