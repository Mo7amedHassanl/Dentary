package com.m7md7sn.dentary.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class ClinicLogoManager @Inject constructor(
    private val storage: Storage,
    private val authRepository: AuthRepository,
    @ApplicationContext
    private val context: Context
) {

    suspend fun uploadClinicLogo(imageUri: Uri, oldImageUrl: String? = null): Result<String, DataError> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    return@withContext Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")
                }

                // Delete old image if it exists
                if (oldImageUrl != null) {
                    try {
                        println("Attempting to delete old clinic logo: $oldImageUrl")
                        deleteClinicLogo(oldImageUrl)
                    } catch (e: Exception) {
                        println("Exception while deleting old clinic logo: ${e.message}")
                    }
                }

                // Create a unique filename
                val fileName = "clinic_logos/${currentUser.id}/${UUID.randomUUID()}.jpg"
                
                // Compress and convert image
                val compressedImageBytes = compressImage(imageUri)
                
                // Upload to Supabase Storage
                try {
                    storage.from("avatars").upload(
                        path = fileName,
                        data = compressedImageBytes
                    )
                } catch (uploadException: Exception) {
                    return@withContext Result.Error(uploadException.toDataError(), "Failed to upload to storage: ${uploadException.message}")
                }

                // Get public URL
                val publicUrl = storage.from("avatars").publicUrl(fileName)

                Result.Success(publicUrl)
            } catch (e: Exception) {
                Result.Error(e.toDataError(), e.message)
            }
        }
    }

    private suspend fun compressImage(imageUri: Uri): ByteArray {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                // Max 1024x1024 for clinic logos (a bit higher quality than profile pics)
                val maxSize = 1024
                val width = originalBitmap.width
                val height = originalBitmap.height
                
                val scaleFactor = if (width > height) {
                    maxSize.toFloat() / width
                } else {
                    maxSize.toFloat() / height
                }
                
                val newWidth = (width * scaleFactor).toInt()
                val newHeight = (height * scaleFactor).toInt()

                val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                
                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                
                originalBitmap.recycle()
                scaledBitmap.recycle()
                
                outputStream.toByteArray()
            } catch (e: Exception) {
                throw Exception("Failed to compress image: ${e.message}")
            }
        }
    }

    suspend fun deleteClinicLogo(imageUrl: String): Result<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            try {
                val pathStart = imageUrl.indexOf("/avatars/")
                if (pathStart == -1) return@withContext Result.Error(DataError.Network.UNKNOWN, "Invalid URL")
                
                val storagePath = imageUrl.substring(pathStart + 9).split("?").first()
                
                storage.from("avatars").delete(storagePath)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e.toDataError(), e.message)
            }
        }
    }
}