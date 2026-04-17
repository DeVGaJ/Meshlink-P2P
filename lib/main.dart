import 'package:flutter/material.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:meshlink/core/constants/colors.dart';
import 'package:meshlink/core/encryption/encryption_service.dart';
import 'package:meshlink/core/network/mesh_network_service.dart';
import 'package:meshlink/data/models/message_model.dart';
import 'package:uuid/uuid.dart';

/// Developed by DeVGaJ - https://github.com/DeVGaJ
/// Core Application Entry Point
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Initialize Hive for Offline Persistence
  await Hive.initFlutter();
  Hive.registerAdapter(MessageAdapter());
  await Hive.openBox<Message>('messages');

  // Initialize Encryption Service
  final encryptionService = EncryptionService();
  await encryptionService.init();

  // Initialize Mesh Network (using a random UUID for the device)
  final deviceId = const Uuid().v4();
  final meshService = MeshNetworkService(
    userName: "Peer_$deviceId",
    serviceId: "meshlink-room-77", // Default Service ID
  );

  runApp(MeshLinkApp(meshService: meshService, encryptionService: encryptionService));
}

class MeshLinkApp extends StatelessWidget {
  final MeshNetworkService meshService;
  final EncryptionService encryptionService;

  const MeshLinkApp({
    super.key, 
    required this.meshService, 
    required this.encryptionService
  });

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MeshLink',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: MeshColors.background,
        primaryColor: MeshColors.primary,
        colorScheme: const ColorScheme.dark(
          primary: MeshColors.primary,
          surface: MeshColors.background,
          onSurface: MeshColors.textPrimary,
        ),
        useMaterial3: true,
      ),
      home: MeshHomeScreen(meshService: meshService),
    );
  }
}

class MeshHomeScreen extends StatefulWidget {
  final MeshNetworkService meshService;
  const MeshHomeScreen({super.key, required this.meshService});

  @override
  State<MeshHomeScreen> createState() => _MeshHomeScreenState();
}

class _MeshHomeScreenState extends State<MeshHomeScreen> {
  bool isNetworkingActive = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("MeshLink", style: TextStyle(fontWeight: FontWeight.bold, color: MeshColors.primary)),
        backgroundColor: Colors.black,
        actions: [
          IconButton(
            icon: Icon(isNetworkingActive ? Icons.wifi_tethering : Icons.wifi_tethering_off),
            color: isNetworkingActive ? MeshColors.primary : Colors.grey,
            onPressed: () async {
              if (isNetworkingActive) {
                // Stop logic here
              } else {
                await widget.meshService.startAdvertising();
                await widget.meshService.startDiscovery();
              }
              setState(() => isNetworkingActive = !isNetworkingActive);
            },
          ),
          IconButton(
            icon: const Icon(Icons.info_outline),
            onPressed: () => _showAbout(),
          ),
        ],
      ),
      body: ValueListenableBuilder(
        valueListenable: Hive.box<Message>('messages').listenable(),
        builder: (context, Box<Message> box, _) {
          final messages = box.values.toList().cast<Message>();
          if (messages.isEmpty) {
            return const Center(child: Text("No secure messages yet.", style: TextStyle(color: MeshColors.textSecondary)));
          }
          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: messages.length,
            itemBuilder: (context, index) {
              final msg = messages[index];
              return _MessageBubble(message: msg);
            },
          );
        },
      ),
      bottomNavigationBar: _buildBottomInput(),
    );
  }

  void _showAbout() {
    showAboutDialog(
      context: context,
      applicationName: 'MeshLink',
      applicationVersion: '1.0.0',
      applicationIcon: const Icon(Icons.hub, color: MeshColors.primary, size: 48),
      children: [
        const Text("Developed by DeVGaJ", style: TextStyle(fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        const Text("Decentralized P2P Mesh Network with E2EE."),
        const Text("https://github.com/DeVGaJ", style: TextStyle(color: MeshColors.primary)),
      ],
    );
  }

  Widget _buildBottomInput() {
    final controller = TextEditingController();
    return Container(
      padding: EdgeInsets.only(bottom: MediaQuery.of(context).viewInsets.bottom + 8, left: 12, right: 12, top: 8),
      color: Colors.black,
      child: Row(
        children: [
          Expanded(
            child: TextField(
              controller: controller,
              decoration: InputDecoration(
                hintText: "Enter secure message...",
                fillColor: MeshColors.cardBackground,
                filled: true,
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(25), borderSide: BorderSide.none),
              ),
            ),
          ),
          const SizedBox(width: 8),
          CircleAvatar(
            backgroundColor: MeshColors.primary,
            child: IconButton(
              icon: const Icon(Icons.send, color: Colors.white),
              onPressed: () {
                if (controller.text.isNotEmpty) {
                  final newMessage = Message(
                    id: const Uuid().v4(),
                    senderId: "me",
                    receiverId: "mesh",
                    content: controller.text,
                    timestamp: DateTime.now(),
                    isMe: true,
                  );
                  Hive.box<Message>('messages').add(newMessage);
                  controller.clear();
                }
              },
            ),
          ),
        ],
      ),
    );
  }
}

class _MessageBubble extends StatelessWidget {
  final Message message;
  const _MessageBubble({required this.message});

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: message.isMe ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 6),
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
        decoration: BoxDecoration(
          color: message.isMe ? MeshColors.bubbleMe : MeshColors.bubbleThem,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: message.isMe ? MeshColors.primary.withOpacity(0.5) : Colors.transparent),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(message.content, style: const TextStyle(color: MeshColors.textPrimary)),
            const SizedBox(height: 4),
            Text(
              "${message.timestamp.hour}:${message.timestamp.minute}", 
              style: const TextStyle(fontSize: 10, color: MeshColors.textSecondary)
            ),
          ],
        ),
      ),
    );
  }
}
