package org.example.app.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
import java.nio.charset.StandardCharsets

class EncryptionManager(private val context: Context) {
    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setKeyGenParameterSpec(
            KeyGenParameterSpec.Builder(
                MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        )
        .build()

    fun encryptString(plaintext: String, fileName: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            val encryptedFile = EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileOutput().use { outputStream ->
                outputStream.write(plaintext.toByteArray(StandardCharsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun decryptString(fileName: String): String? {
        return try {
            val file = File(context.filesDir, fileName)
            val encryptedFile = EncryptedFile.Builder(
                context,
                file,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileInput().use { inputStream ->
                String(inputStream.readBytes(), StandardCharsets.UTF_8)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun deleteEncryptedFile(fileName: String): Boolean {
        return try {
            val file = File(context.filesDir, fileName)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
}
