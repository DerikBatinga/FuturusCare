package com.example.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {
    private const val ITERATIONS = 10_000
    private const val KEY_LENGTH_BITS = 256

    fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun hashPassword(password: String, saltBase64: String): String {
        val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
        return try {
            val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS)
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val encoded = factory.generateSecret(spec).encoded
            Base64.encodeToString(encoded, Base64.NO_WRAP)
        } catch (_: Exception) {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(salt)
            val encoded = digest.digest(password.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encoded, Base64.NO_WRAP)
        }
    }

    fun verifyPassword(password: String, saltBase64: String, expectedHash: String): Boolean {
        val computed = hashPassword(password, saltBase64)
        return MessageDigest.isEqual(
            computed.toByteArray(Charsets.UTF_8),
            expectedHash.toByteArray(Charsets.UTF_8)
        )
    }
}
