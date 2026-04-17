package com.devgaj.meshlink.core.encryption

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import java.util.Base64

/**
 * Developed by DeVGaJ - https://github.com/DeVGaJ
 * Handles E2EE using Google Tink (AES-GCM).
 */
class EncryptionService(private val context: Context) {

    init {
        AeadConfig.register()
    }

    // Use a fixed key for development so devices can decrypt each other's messages
    // In production, this would be exchanged via a handshake
    private val KEY_JSON = "{\"primaryKeyId\":1043213070,\"key\":[{\"keyData\":{\"typeUrl\":\"type.googleapis.com/google.crypto.tink.AesGcmKey\",\"value\":\"GiB7mY8p+PZ3mGj6kH/Hn4mY8p+PZ3mGj6kH/Hn4mY8p+P==\",\"keyMaterialType\":\"SYMMETRIC\"},\"status\":\"ENABLED\",\"keyId\":1043213070,\"outputPrefixType\":\"TINK\"}]}"

    private val keysetHandle: KeysetHandle by lazy {
        com.google.crypto.tink.CleartextKeysetHandle.read(
            com.google.crypto.tink.JsonKeysetReader.withString(KEY_JSON)
        )
    }

    private val aead: Aead by lazy {
        keysetHandle.getPrimitive(Aead::class.java)
    }

    /**
     * Encrypts a plain text message.
     */
    fun encrypt(plainText: String): String {
        val ciphertext = aead.encrypt(plainText.toByteArray(), null)
        return Base64.getEncoder().encodeToString(ciphertext)
    }

    /**
     * Decrypts a cipher text message.
     */
    fun decrypt(cipherText: String): String {
        val decoded = Base64.getDecoder().decode(cipherText)
        val decrypted = aead.decrypt(decoded, null)
        return String(decrypted)
    }
}
