package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.data.source.remote.ProfileDataSource
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val profilePictureManager: ProfilePictureManager,
    private val clinicLogoManager: ClinicLogoManager
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile, DataError> {
        return profileDataSource.getProfile()
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile, DataError> {
        return profileDataSource.createProfile(request)
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile, DataError> {
        return profileDataSource.updateProfile(request)
    }

    override suspend fun updateProfilePicture(imageUri: Uri, oldImageUrl: String?): Result<Profile, DataError> {
        // Upload image to storage
        val uploadResult = profilePictureManager.uploadProfilePicture(imageUri, oldImageUrl)
        
        return when (uploadResult) {
            is Result.Success -> {
                val imageUrl = uploadResult.data
                
                // Update profile with new image URL
                val updateRequest = UpdateProfileRequest(profilePicture = imageUrl)
                profileDataSource.updateProfile(updateRequest)
            }
            is Result.Error -> {
                Result.Error(uploadResult.error, uploadResult.message ?: "Failed to upload profile picture")
            }
            is Result.Loading -> Result.Loading
        }
    }

    override suspend fun updateClinicLogo(imageUri: Uri, oldImageUrl: String?): Result<Profile, DataError> {
        // Upload logo to storage
        val uploadResult = clinicLogoManager.uploadClinicLogo(imageUri, oldImageUrl)
        
        return when (uploadResult) {
            is Result.Success -> {
                val imageUrl = uploadResult.data
                
                // Update profile with new logo URL
                val updateRequest = UpdateProfileRequest(clinicLogo = imageUrl)
                profileDataSource.updateProfile(updateRequest)
            }
            is Result.Error -> {
                Result.Error(uploadResult.error, uploadResult.message ?: "Failed to upload clinic logo")
            }
            is Result.Loading -> Result.Loading
        }
    }
}