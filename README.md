<p align="center">
  <img src="https://i.imgur.com/a0vGWwL.jpeg" width="150" alt="MeshLink Logo">
</p>

<h1 align="center">MeshLink</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/Security-E2EE-blueviolet.svg" alt="Security">
  <img src="https://img.shields.io/badge/Status-Educational-orange.svg" alt="Status">
</p>

<p align="center">
  <strong>Secure, Off-Grid Peer-to-Peer Messaging</strong>
</p>

---

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Security-Tink-blue?style=for-the-badge" />
</p>

**MeshLink** is a decentralized, peer-to-peer (P2P) communication platform designed for **secure, internet-free messaging**. By creating a local mesh network, MeshLink allows users to exchange data in environments where Wi-Fi, cellular networks, or central servers are unavailable or untrusted.

---

## 🚀 Key Features

* **Zero-Infrastructure:** No internet, no SIM cards, and no central servers required.
* **Automatic Discovery:** Seamlessly find and connect to nearby peers using **Google Nearby Connections**.
* **End-to-End Encryption:** All traffic is secured via the **Google Tink** library using **AES-GCM** and **X25519**.
* **Background Resilience:** Uses Android Services to maintain connectivity even when the app is not in the foreground.
* **Privacy-Centric:** Designed to be anonymous-by-default with no digital footprint left on external servers.

---

## 🛠 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose (Declarative UI) |
| **Networking** | Google Nearby Connections API (P2P / Mesh) |
| **Cryptography** | Google Tink (Authenticated Encryption) |
| **Concurrency** | Kotlin Coroutines & Flow |

---

## 📦 Getting Started

### Prerequisites
* Android device running **API 24 (Nougat)** or higher.
* Bluetooth and Location services enabled (required for peer discovery).

### Installation
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/DeVGaJ/MeshLink.git](https://github.com/DeVGaJ/MeshLink.git)
    ```
2.  **Open in Android Studio:**
    Import the project and sync the Gradle files.
3.  **Build and Run:**
    Deploy the app to two or more Android devices to test the mesh connectivity.

---

## 🛡 Security & Educational Disclaimer

> [!IMPORTANT]
> This project is developed for **educational and research purposes**. While it implements robust encryption standards (AES-GCM), it has not undergone a formal professional security audit. 
> 
> **Do not use MeshLink for:**
> * Transmitting highly sensitive personal or financial data.
> * Life-critical communication in high-risk emergency environments.
>
> The developer is not responsible for any data loss or misuse of this software.

---

## 🤝 Contributing

MeshLink is open-source! Whether it's fixing bugs, improving the mesh protocol, or refining the UI/UX, contributions are welcome.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 👨‍💻 Developer
Developed with ❤️ by **[DeVGaJ](https://github.com/DeVGaJ)**

*Android Developer | UI/UX Designer*
