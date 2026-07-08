package com.m7md7sn.dentary.data.util

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalImageStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val imagesDir = File(context.filesDir, "patient_images").apply {
        if (!exists()) mkdirs()
    }

    suspend fun saveImageLocally(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val destFile = File(imagesDir, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteLocalImage(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
