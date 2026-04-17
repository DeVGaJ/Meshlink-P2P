package com.devgaj.meshlink

import android.Manifest
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devgaj.meshlink.core.encryption.EncryptionService
import com.devgaj.meshlink.core.network.MeshNetworkService
import com.devgaj.meshlink.ui.screens.MeshChatScreen
import com.devgaj.meshlink.ui.theme.MeshlinkTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

/**
 * MeshLink - Developed by DeVGaJ
 * GitHub: https://github.com/DeVGaJ
 */
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class MainActivity : ComponentActivity() {

    private lateinit var meshService: MeshNetworkService
    private lateinit var encryptionService: EncryptionService
    private val messages = mutableStateListOf("Welcome to MeshLink.", "Secure P2P Mesh Active.")
    private var connectionStatus = mutableStateOf("Disconnected")

    private val locationSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            meshService.startMesh()
            connectionStatus.value = "Searching..."
        } else {
            Toast.makeText(this, "Location services are required", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            checkAndEnableLocationSettings()
        } else {
            Toast.makeText(this, "Permissions required for P2P networking", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uniqueName = "User_${Build.MODEL}_${(1000..9999).random()}"
        encryptionService = EncryptionService(this)
        meshService = MeshNetworkService(this, uniqueName)

        // Handle incoming messages
        meshService.onMessageReceived = { encryptedMessage ->
            try {
                val decrypted = encryptionService.decrypt(encryptedMessage)
                runOnUiThread {
                    messages.add("Peer: $decrypted")
                }
            } catch (e: Exception) {
                Log.e("MeshLink", "Decryption failed", e)
            }
        }

        meshService.onSystemMessage = { systemMsg ->
            runOnUiThread {
                messages.add("System: $systemMsg")
                if (systemMsg.contains("Connected")) {
                    connectionStatus.value = "Connected"
                }
            }
        }

        setContent {
            MeshlinkTheme {
                MeshChatScreen(
                    userName = uniqueName,
                    messages = messages,
                    status = connectionStatus.value,
                    onSendMessage = { text ->
                        val encrypted = encryptionService.encrypt(text)
                        meshService.sendEncryptedMessage(encrypted)
                    }
                )
            }
        }

        requestMeshPermissions()
    }

    private fun checkAndEnableLocationSettings() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            meshService.startMesh()
            connectionStatus.value = "Searching..."
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettingsLauncher.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.e("MeshLink", "Error opening location settings", sendEx)
                }
            } else {
                Toast.makeText(this, "Location services unavailable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        meshService.stopMesh()
    }

    private fun requestMeshPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        requestPermissionLauncher.launch(permissions.toTypedArray())
    }
}
