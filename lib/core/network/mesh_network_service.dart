import 'dart:typed_data';
import 'package:nearby_connections/nearby_connections.dart';

/// Developed by DeVGaJ - https://github.com/DeVGaJ
/// Manages P2P connections using Google's Nearby Connections API.
class MeshNetworkService {
  final Strategy strategy = Strategy.P2P_CLUSTER;
  final String userName;
  final String serviceId; // e.g., "meshlink-room-77"

  // Maps to track connected peers
  final Map<String, ConnectionInfo> connectedPeers = {};

  MeshNetworkService({required this.userName, required this.serviceId});

  /// Start broadcasting presence for others to find.
  Future<void> startAdvertising() async {
    try {
      bool a = await Nearby().startAdvertising(
        userName,
        strategy,
        onConnectionInitiated: onConnectionInitiated,
        onConnectionResult: (id, status) {
          if (status == Status.CONNECTED) {
            print("Connected to $id");
          } else {
            print("Connection failed to $id");
          }
        },
        onDisconnected: (id) {
          connectedPeers.remove(id);
          print("Disconnected from $id");
        },
        serviceId: serviceId,
      );
      print("ADVERTISING: $a");
    } catch (e) {
      print("Error starting advertising: $e");
    }
  }

  /// Start searching for other devices broadcasting the same Service ID.
  Future<void> startDiscovery() async {
    try {
      bool a = await Nearby().startDiscovery(
        userName,
        strategy,
        onEndpointFound: (id, name, serviceId) {
          // Auto-connect to peers in the same mesh cluster
          Nearby().requestConnection(
            userName,
            id,
            onConnectionInitiated: onConnectionInitiated,
            onConnectionResult: (id, status) {
              if (status == Status.CONNECTED) {
                print("Connected to $id");
              }
            },
            onDisconnected: (id) {
              connectedPeers.remove(id);
            },
          );
        },
        onEndpointLost: (id) {},
        serviceId: serviceId,
      );
      print("DISCOVERY: $a");
    } catch (e) {
      print("Error starting discovery: $e");
    }
  }

  /// Callback when a connection is initiated (from either side).
  void onConnectionInitiated(String id, ConnectionInfo info) {
    connectedPeers[id] = info;
    // For MeshLink, we accept all connections automatically within the Service ID cluster
    Nearby().acceptConnection(
      id,
      onPayLoadRecieved: (endpointId, payload) {
        if (payload.type == PayloadType.BYTES) {
          _handleIncomingPayload(endpointId, payload.bytes!);
        }
      },
      onPayloadTransferUpdate: (endpointId, payloadTransferUpdate) {},
    );
  }

  /// Internal handler for routing and processing packets.
  void _handleIncomingPayload(String fromId, Uint8List data) {
    // Basic Mesh Routing Logic Placeholder:
    // 1. Check if packet is destined for this node.
    // 2. If yes, decrypt and display.
    // 3. If no, look up destination in routing table and forward.
    print("Received packet from $fromId");
  }

  /// Send encrypted data to a specific peer.
  Future<void> sendPayload(String endpointId, String encryptedMessage) async {
    await Nearby().sendBytesPayload(
      endpointId,
      Uint8List.fromList(encryptedMessage.codeUnits),
    );
  }
}
