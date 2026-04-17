import 'package:hive/hive.dart';

part 'message_model.g.dart';

/// Developed by DeVGaJ - https://github.com/DeVGaJ
@HiveType(typeId: 0)
class Message extends HiveObject {
  @HiveField(0)
  final String id;

  @HiveField(1)
  final String senderId;

  @HiveField(2)
  final String receiverId;

  @HiveField(3)
  final String content; // Encrypted or Decrypted depending on state

  @HiveField(4)
  final DateTime timestamp;

  @HiveField(5)
  final bool isMe;

  Message({
    required this.id,
    required this.senderId,
    required this.receiverId,
    required this.content,
    required this.timestamp,
    required this.isMe,
  });
}
