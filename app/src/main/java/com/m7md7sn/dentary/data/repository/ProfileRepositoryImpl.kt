package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.data.source.remote.ProfileDataSource
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val profilePictureManager: ProfilePictureManager
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile> {
        return profileDataSource.getProfile()
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile> {
        return profileDataSource.createProfile(request)
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> {
        return profileDataSource.updateProfile(request)
    }

    override suspend fun updateProfilePicture(imageUri: Uri, oldImageUrl: String?): Result<Profile> {
        return try {
            // Upload image to storage
            val uploadResult = profilePictureManager.uploadProfilePicture(imageUri, oldImageUrl)
            
            when (uploadResult) {
                is Result.Success -> {
                    val imageUrl = uploadResult.data
                    
                    // Update profile with new image URL
                    val updateRequest = UpdateProfileRequest(profilePicture = imageUrl)
                    profileDataSource.updateProfile(updateRequest)
                }
                is Result.Error -> {
                    Result.Error(uploadResult.message ?: "Failed to upload profile picture")
                }
                else -> {
                    Result.Error("Unknown error occurred during upload")
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update profile picture")
        }
    }
}
