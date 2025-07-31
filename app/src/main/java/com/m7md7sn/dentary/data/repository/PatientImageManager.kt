package com.m7md7sn.dentary.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class PatientImageManager @Inject constructor(
    private val storage: Storage,
    private val authRepository: AuthRepository,
    @ApplicationContext
    private val context: Context
) {

    suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String? = null): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    return@withContext Result.Error("User not authenticated")
                }

                // Delete old image if it exists
                if (oldImageUrl != null) {
                    try {
                        println("Attempting to delete old patient image: $oldImageUrl")
                        val deleteResult = deletePatientImage(oldImageUrl)
                        when (deleteResult) {
                            is Result.Success -> {
                                println("Successfully deleted old patient image")
                            }
                            is Result.Error -> {
                                println("Failed to delete old patient image: ${deleteResult.message}")
                            }
                            is Result.Loading -> {
                                println("Delete operation is loading (unexpected)")
                            }
                        }
                    } catch (e: Exception) {
                        // Log the error but continue with upload
                        println("Exception while deleting old patient image: ${e.message}")
                    }
                }

                // Create a unique filename
                val fileName = "patient_images/${currentUser.id}/${UUID.randomUUID()}.jpg"
                
                // Compress and convert image
                val compressedImageBytes = compressImage(imageUri)
                
                // Upload to Supabase Storage
                try {
                    storage.from("avatars").upload(
                        path = fileName,
                        data = compressedImageBytes
                    )
                } catch (uploadException: Exception) {
                    return@withContext Result.Error("Failed to upload to storage: ${uploadException.message}")
                }

                // Get public URL
                val publicUrl = storage.from("avatars").publicUrl(fileName)

                Result.Success(publicUrl)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Failed to upload patient image")
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

                // Calculate new dimensions (max 512x512 for patient images)
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

    suspend fun deletePatientImage(imageUrl: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    return@withContext Result.Error("User not authenticated")
                }

                println("Deleting patient image with URL: $imageUrl")

                // Extract the path from Supabase URL
                // Supabase URLs look like: https://xxx.supabase.co/storage/v1/object/public/avatars/patient_images/user_id/filename.jpg
                val pathStart = imageUrl.indexOf("/avatars/")
                if (pathStart == -1) {
                    return@withContext Result.Error("Invalid Supabase URL format")
                }
                
                val storagePath = imageUrl.substring(pathStart + 9) // Skip "/avatars/"
                
                // Remove any query parameters
                val cleanPath = storagePath.split("?").first()
                
                println("Extracted storage path: $cleanPath")

                // Check if file exists before attempting deletion
                val fileExists = fileExistsInStorage(cleanPath)
                println("File exists check: $fileExists")

                // Try multiple deletion strategies
                var deletionSuccessful = false
                var lastError = ""

                // Strategy 1: Direct path deletion
                try {
                    println("Trying direct path deletion: $cleanPath")
                    storage.from("avatars").delete(cleanPath)
                    println("Direct deletion successful")
                    deletionSuccessful = true
                } catch (e: Exception) {
                    lastError = "Direct deletion failed: ${e.message}"
                    println(lastError)
                }

                // Strategy 2: Extract filename and reconstruct path
                if (!deletionSuccessful) {
                    try {
                        val fileName = imageUrl.substringAfterLast("/").split("?").first()
                        val reconstructedPath = "patient_images/${currentUser.id}/$fileName"
                        println("Trying reconstructed path: $reconstructedPath")
                        
                        storage.from("avatars").delete(reconstructedPath)
                        println("Reconstructed path deletion successful")
                        deletionSuccessful = true
                    } catch (e: Exception) {
                        lastError = "Reconstructed path deletion failed: ${e.message}"
                        println(lastError)
                    }
                }

                // Strategy 3: Try with just the filename
                if (!deletionSuccessful) {
                    try {
                        val fileName = imageUrl.substringAfterLast("/").split("?").first()
                        println("Trying filename-only deletion: $fileName")
                        
                        storage.from("avatars").delete(fileName)
                        println("Filename-only deletion successful")
                        deletionSuccessful = true
                    } catch (e: Exception) {
                        lastError = "Filename-only deletion failed: ${e.message}"
                        println(lastError)
                    }
                }

                // Strategy 4: Try to list files and find the exact path
                if (!deletionSuccessful) {
                    try {
                        println("Trying to list files to find exact path...")
                        val files = storage.from("avatars").list("patient_images/${currentUser.id}")
                        println("Found files: ${files.map { it.name }}")
                        
                        // Look for files that match our pattern
                        val targetFileName = imageUrl.substringAfterLast("/").split("?").first()
                        val matchingFile = files.find { it.name == targetFileName }
                        
                        if (matchingFile != null) {
                            val exactPath = "patient_images/${currentUser.id}/${matchingFile.name}"
                            println("Found exact path: $exactPath")
                            storage.from("avatars").delete(exactPath)
                            println("Exact path deletion successful")
                            deletionSuccessful = true
                        } else {
                            println("No matching file found in listing")
                        }
                    } catch (e: Exception) {
                        lastError = "File listing and exact path deletion failed: ${e.message}"
                        println(lastError)
                    }
                }

                if (deletionSuccessful) {
                    println("Deletion completed successfully")
                    Result.Success(Unit)
                } else {
                    println("All deletion strategies failed. Last error: $lastError")
                    Result.Error("Failed to delete patient image after trying all strategies: $lastError")
                }
            } catch (e: Exception) {
                println("Exception in deletePatientImage: ${e.message}")
                Result.Error(e.message ?: "Failed to delete patient image")
            }
        }
    }

    private suspend fun fileExistsInStorage(filePath: String): Boolean {
        return try {
            storage.from("avatars").list(filePath.substringBeforeLast("/"))
                .any { it.name == filePath.substringAfterLast("/") }
        } catch (e: Exception) {
            println("Error checking if file exists: ${e.message}")
            false
        }
    }
} 