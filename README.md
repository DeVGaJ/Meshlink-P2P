# MeshLink

**Developed by DeVGaJ**  
[GitHub Profile](https://github.com/DeVGaJ)

MeshLink is a decentralized, offline, peer-to-peer (P2P) messaging application built with Flutter. It enables communication in environments without internet access or cellular coverage by creating a mesh network of devices.

## Features
- **P2P Mesh Networking:** Uses Google's Nearby Connections API with `P2P_CLUSTER` strategy.
- **End-to-End Encryption (E2EE):** Secure communication using X25519 key exchange and AES-GCM encryption.
- **Service ID Routing:** Join specific clusters using alphanumeric invite links.
- **Nightshade Theme:** AMOLED-friendly true black theme for maximum battery efficiency.
- **Fast Persistence:** Powered by Hive for ultra-fast local storage.

## Architecture
- **MeshNetworkService:** Manages device discovery, advertising, and payload routing.
- **EncryptionService:** Handles cryptographic key generation and secure message transformation.
- **Modular UI:** Clean separation of concerns following standard Flutter practices.

## Requirements
- Flutter SDK (>= 3.0.0)
- Android device with Bluetooth, BLE, and Wi-Fi capability.
- Location permissions (required for P2P discovery).

## Branding
Developed with ❤️ by **DeVGaJ**.
Official Repository: [https://github.com/DeVGaJ](https://github.com/DeVGaJ)
