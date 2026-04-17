package com.devgaj.meshlink

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.devgaj.meshlink.core.encryption.EncryptionService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Developed by DeVGaJ - Senior QA Automation Engineer
 * Unit Test Suite for E2EE Logic.
 */
@RunWith(RobolectricTestRunner::class)
class EncryptionServiceTest {

    private lateinit var encryptionService: EncryptionService
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        encryptionService = EncryptionService(context)
    }

    @Test
    fun `test Encryption and Decryption integrity`() {
        val originalMessage = "Hello MeshLink P2P"
        
        // 1. Encrypt
        val encryptedMessage = encryptionService.encrypt(originalMessage)
        
        // Assert that ciphertext is different from plaintext
        assertNotEquals(originalMessage, encryptedMessage)
        
        // 2. Decrypt
        val decryptedMessage = encryptionService.decrypt(encryptedMessage)
        
        // 3. Verify match
        assertEquals(originalMessage, decryptedMessage)
    }

    @Test
    fun `test different ciphertexts for same plaintext (AES-GCM Nonce)`() {
        val message = "Constant Message"
        val firstEncryption = encryptionService.encrypt(message)
        val secondEncryption = encryptionService.encrypt(message)
        
        // AES-GCM should produce different ciphertexts due to internal IV/Nonce even for same key
        assertNotEquals(firstEncryption, secondEncryption)
        
        // But both must decrypt to the same original message
        assertEquals(message, encryptionService.decrypt(firstEncryption))
        assertEquals(message, encryptionService.decrypt(secondEncryption))
    }
}
