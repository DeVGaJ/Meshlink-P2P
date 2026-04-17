<p align="center">
  <img src="logo.png" width="120" alt="MeshLink Logo">
</p>

<h1 align="center">MeshLink</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Active-brightgreen.svg" alt="Status">
  <img src="https://img.shields.io/badge/Security-E2EE-blueviolet.svg" alt="Security">
  <img src="https://img.shields.io/badge/Connectivity-Offline_Mesh-blue.svg" alt="Connectivity">
  <img src="https://img.shields.io/badge/License-Educational-orange.svg" alt="License">
</p>

<p align="center">
  <strong>No Internet. No Servers. Just Secure Peer-to-Peer Messaging.</strong>
</p>

---

### 🚀 The Concept
MeshLink is a "zero-trust" communication tool. It bypasses the internet entirely by turning your phone into a node in a local mesh network. It’s built for situations where traditional networks fail or cannot be trusted.

### 🎯 Core Purpose
*   **Direct P2P:** Chat device-to-device via Bluetooth & Wi-Fi.
*   **Encrypted by Default:** Every byte sent is locked with military-grade encryption.
*   **Auto-Sync:** Discover nearby peers automatically as soon as you open the app.

### 🛠 How it Works
1.  **Discovery:** The app uses Google's Nearby API to find other MeshLink users within range (up to 100m).
2.  **Handshake:** Devices exchange public keys securely to establish a trusted connection.
3.  **Communication:** Messages are encrypted locally, sent over the air, and decrypted only by the recipient.

---

### 📊 System Architecture & Technology Stack

| Component | Technology | Purpose |
| :--- | :--- | :--- |
| **User Interface** | **Jetpack Compose** | Modern, reactive UI with a "true black" AMOLED theme. |
| **P2P Networking** | **Google Nearby Connections** | High-bandwidth, low-latency offline communication (P2P_CLUSTER). |
| **Cryptography** | **Google Tink** | Hybrid encryption (X25519 + AES-GCM) for bulletproof security. |
| **Database** | **Room Persistence** | High-performance local message logging and peer management. |
| **Permissions** | **Location & Bluetooth** | Automated system for seamless radio hardware access. |
| **Language** | **Kotlin Coroutines** | Asynchronous, non-blocking network operations for zero lag. |

---

### ⚠️ Educational Warning
**DISCLAIMER:** This project is developed for **educational and research purposes only**. While it uses industry-standard encryption, it has not been audited by security professionals. Do not use this for life-critical communication in high-risk zones. The author is not liable for data loss or security breaches.

---

**Developed with ❤️ by [DeVGaJ](https://github.com/DeVGaJ)**
