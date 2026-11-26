package com.example.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class StoredDocument(
    val filePath: String,
    val fileName: String,
    val isImage: Boolean
)

object ImageStorageHelper {
    suspend fun copyUriToInternalStorage(
        context: Context,
        uri: Uri,
        prefix: String
    ): String? = withContext(Dispatchers.IO) {
        runCatching {
            val mediaDir = File(context.filesDir, "petcare_media").apply { mkdirs() }
            val destFile = File(mediaDir, "${prefix}_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            if (destFile.exists() && destFile.length() > 0L) destFile.absolutePath else null
        }.getOrNull()
    }

    suspend fun copyCertificateToInternalStorage(
        context: Context,
        uri: Uri
    ): StoredDocument? = withContext(Dispatchers.IO) {
        runCatching {
            val resolver = context.contentResolver
            val mimeType = resolver.getType(uri).orEmpty()
            var displayName = "certificado_${System.currentTimeMillis()}"
            resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) {
                        val queried = cursor.getString(idx)
                        if (!queried.isNullOrBlank()) {
                            displayName = queried
                        }
                    }
                }
            }

            val isImage = mimeType.startsWith("image/") ||
                displayName.endsWith(".jpg", ignoreCase = true) ||
                displayName.endsWith(".jpeg", ignoreCase = true) ||
                displayName.endsWith(".png", ignoreCase = true) ||
                displayName.endsWith(".webp", ignoreCase = true)

            val ext = when {
                mimeType.contains("pdf", ignoreCase = true) || displayName.endsWith(".pdf", ignoreCase = true) -> ".pdf"
                displayName.endsWith(".png", ignoreCase = true) -> ".png"
                displayName.endsWith(".webp", ignoreCase = true) -> ".webp"
                isImage -> ".jpg"
                else -> ".pdf"
            }

            val certDir = File(context.filesDir, "petcare_certificates").apply { mkdirs() }
            val destFile = File(certDir, "cert_${System.currentTimeMillis()}$ext")
            resolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            if (destFile.exists() && destFile.length() > 0L) {
                StoredDocument(
                    filePath = destFile.absolutePath,
                    fileName = displayName,
                    isImage = isImage
                )
            } else {
                null
            }
        }.getOrNull()
    }
}
