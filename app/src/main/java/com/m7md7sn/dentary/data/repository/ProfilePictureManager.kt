package com.m7md7sn.dentary.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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

class ProfilePictureManager @Inject constructor(
    private val storage: Storage,
    private val authRepository: AuthRepository,
    @ApplicationContext
    private val context: Context
) {

    suspend fun uploadProfilePicture(imageUri: Uri, oldImageUrl: String? = null): Result<String, DataError> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    return@withContext Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")
                }

                // Delete old image if it exists
                if (oldImageUrl != null) {
                    try {
                        Log.d("ProfilePicMgr", "Attempting to delete old image: $oldImageUrl")
                        val deleteResult = deleteProfilePicture(oldImageUrl)
                        when (deleteResult) {
                            is Result.Success -> {
                                Log.d("ProfilePicMgr", "Successfully deleted old image")
                                // Add a small delay to ensure deletion is processed
                                kotlinx.coroutines.delay(1000)
                            }
                            is Result.Error -> {
                                Log.w("ProfilePicMgr", "Failed to delete old image: ${deleteResult.message}")
                            }
                            is Result.Loading -> {
                                Log.w("ProfilePicMgr", "Delete operation is loading (unexpected)")
                            }
                        }
                    } catch (e: Exception) {
                        // Log the error but continue with upload
                        Log.w("ProfilePicMgr", "Exception while deleting old image: ${e.message}")
                    }
                }

                // Create a unique filename
                val fileName = "profile_pictures/${currentUser.id}/${UUID.randomUUID()}.jpg"
                
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
                // Read the original image
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (originalBitmap == null) {
                    throw Exception("Failed to decode image — file may be corrupt or unsupported format")
                }

                // Calculate new dimensions (max 512x512 for profile pictures)
                val maxSize = 512
                val width = originalBitmap.width
                val height = originalBitmap.height
                
                val scaleFactor = if (width > height) {
                    maxSize.toFloat() / width
                } else {
                    maxSize.toFloat() / height
                }
                
                val newWidth = (width * scaleFactor).toInt()
                val newHeight = (height * scaleFactor).toInt()

                // Create scaled bitmap
                val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
                
                // Compress to JPEG with 80% quality
                val outputStream = ByteArrayOutputStream()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                
                // Clean up
                originalBitmap.recycle()
                scaledBitmap.recycle()
                
                outputStream.toByteArray()
            } catch (e: Exception) {
                throw Exception("Failed to compress image: ${e.message}")
            }
        }
    }

    private suspend fun fileExistsInStorage(filePath: String): Boolean {
        return try {
            storage.from("avatars").list(filePath.substringBeforeLast("/"))
                .any { it.name == filePath.substringAfterLast("/") }
        } catch (e: Exception) {
            Log.w("ProfilePicMgr", "Error checking if file exists: ${e.message}")
            false
        }
    }

    suspend fun deleteProfilePicture(imageUrl: String): Result<Unit, DataError> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    return@withContext Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")
                }

                Log.d("ProfilePicMgr", "Deleting image with URL: $imageUrl")

                // Extract the path from Supabase URL
                val pathStart = imageUrl.indexOf("/avatars/")
                if (pathStart == -1) {
                    return@withContext Result.Error(DataError.Network.UNKNOWN, "Invalid Supabase URL format")
                }
                
                val storagePath = imageUrl.substring(pathStart + 9) // Skip "/avatars/"
                
                // Remove any query parameters
                val cleanPath = storagePath.split("?").first()
                
                Log.d("ProfilePicMgr", "Extracted storage path: $cleanPath")

                // Check if file exists before attempting deletion
                val fileExists = fileExistsInStorage(cleanPath)
                Log.d("ProfilePicMgr", "File exists check: $fileExists")

                // Try multiple deletion strategies
                var deletionSuccessful = false
                var lastError = ""

                // Strategy 1: Direct path deletion
                try {
                    Log.d("ProfilePicMgr", "Trying direct path deletion: $cleanPath")
                    storage.from("avatars").delete(cleanPath)
                    Log.d("ProfilePicMgr", "Direct deletion successful")
                    deletionSuccessful = true
                } catch (e: Exception) {
                    lastError = "Direct deletion failed: ${e.message}"
                    Log.w("ProfilePicMgr", lastError)
                }

                // Strategy 2: Extract filename and reconstruct path
                if (!deletionSuccessful) {
                    try {
                        val fileName = imageUrl.substringAfterLast("/").split("?").first()
                        val reconstructedPath = "profile_pictures/${currentUser.id}/$fileName"
                        Log.d("ProfilePicMgr", "Trying reconstructed path: $reconstructedPath")
                        
                        storage.from("avatars").delete(reconstructedPath)
                        Log.d("ProfilePicMgr", "Reconstructed path deletion successful")
                        deletionSuccessful = true
                    } catch (e: Exception) {
                        lastError = "Reconstructed path deletion failed: ${e.message}"
                        Log.w("ProfilePicMgr", lastError)
                    }
                }

                // Strategy 3: Try with just the filename
                if (!deletionSuccessful) {
                    try {
                        val fileName = imageUrl.substringAfterLast("/").split("?").first()
                        Log.d("ProfilePicMgr", "Trying filename-only deletion: $fileName")
                        
                        storage.from("avatars").delete(fileName)
                        Log.d("ProfilePicMgr", "Filename-only deletion successful")
                        deletionSuccessful = true
                    } catch (e: Exception) {
                        lastError = "Filename-only deletion failed: ${e.message}"
                        Log.w("ProfilePicMgr", lastError)
                    }
                }

                // Strategy 4: Try to list files and find the exact path
                if (!deletionSuccessful) {
                    try {
                        Log.d("ProfilePicMgr", "Trying to list files to find exact path...")
                        val files = storage.from("avatars").list("profile_pictures/${currentUser.id}")
                        Log.d("ProfilePicMgr", "Found files: ${files.map { it.name }}")
                        
                        // Look for files that match our pattern
                        val targetFileName = imageUrl.substringAfterLast("/").split("?").first()
                        val matchingFile = files.find { it.name == targetFileName }
                        
                        if (matchingFile != null) {
                            val exactPath = "profile_pictures/${currentUser.id}/${matchingFile.name}"
                            Log.d("ProfilePicMgr", "Found exact path: $exactPath")
                            storage.from("avatars").delete(exactPath)
                            Log.d("ProfilePicMgr", "Exact path deletion successful")
                            deletionSuccessful = true
                        } else {
                            Log.d("ProfilePicMgr", "No matching file found in listing")
                        }
                    } catch (e: Exception) {
                        lastError = "File listing and exact path deletion failed: ${e.message}"
                        Log.w("ProfilePicMgr", lastError)
                    }
                }

                if (deletionSuccessful) {
                    Log.d("ProfilePicMgr", "Deletion completed successfully")
                    Result.Success(Unit)
                } else {
                    Log.w("ProfilePicMgr", "All deletion strategies failed. Last error: $lastError")
                    Result.Error(DataError.Network.UNKNOWN, "Failed to delete profile picture after trying all strategies: $lastError")
                }
            } catch (e: Exception) {
                Log.e("ProfilePicMgr", "Exception in deleteProfilePicture", e)
                Result.Error(e.toDataError(), e.message)
            }
        }
    }
}