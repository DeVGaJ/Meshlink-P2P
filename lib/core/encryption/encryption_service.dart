import 'dart:convert';
import 'package:cryptography/cryptography.dart';

/// Developed by DeVGaJ - https://github.com/DeVGaJ
/// Handles E2EE using X25519 for key exchange and AES-GCM for encryption.
class EncryptionService {
  final algorithm = AesGcm.with256bits();
  late SimpleKeyPair _keyPair;
  late PublicKey _publicKey;

  // Initialize keys
  Future<void> init() async {
    final x25519 = X25519();
    _keyPair = await x25519.newKeyPair();
    _publicKey = await _keyPair.extractPublicKey();
  }

  // Get the public key to share with peers
  Future<String> getEncodedPublicKey() async {
    final bytes = (await _publicKey.extract()).bytes;
    return base64Encode(bytes);
  }

  // Generate a shared secret for a specific peer
  Future<SecretKey> deriveSharedSecret(String peerEncodedPublicKey) async {
    final x25519 = X25519();
    final peerKeyBytes = base64Decode(peerEncodedPublicKey);
    final peerPublicKey = SimplePublicKey(peerKeyBytes, type: KeyPairType.x25519);
    
    return await x25519.sharedSecretKey(
      keyPair: _keyPair,
      remotePublicKey: peerPublicKey,
    );
  }

  // Encrypt a message string
  Future<String> encrypt(String text, SecretKey sharedSecret) async {
    final clearText = utf8.encode(text);
    final secretBox = await algorithm.encrypt(
      clearText,
      secretKey: sharedSecret,
    );
    return base64Encode(secretBox.concatenation());
  }

  // Decrypt a message string
  Future<String> decrypt(String cipherText, SecretKey sharedSecret) async {
    final cipherBytes = base64Decode(cipherText);
    final secretBox = SecretBox.fromConcatenation(
      cipherBytes,
      nonceLength: algorithm.nonceLength,
      macLength: algorithm.macAlgorithm.macLength,
    );
    
    final clearBytes = await algorithm.decrypt(
      secretBox,
      secretKey: sharedSecret,
    );
    return utf8.decode(clearBytes);
  }
}
