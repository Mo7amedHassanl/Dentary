package com.m7md7sn.dentary.data.repository

import android.net.Uri
import android.util.Log
import androidx.work.WorkManager
import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.data.source.local.dao.ProfileDao
import com.m7md7sn.dentary.data.source.local.entity.toDomain
import com.m7md7sn.dentary.data.source.local.entity.toEntity
import com.m7md7sn.dentary.data.source.remote.ProfileDataSource
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.data.util.LocalImageStorage
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val profileDao: ProfileDao,
    private val auth: Auth,
    private val profilePictureManager: ProfilePictureManager,
    private val clinicLogoManager: ClinicLogoManager,
    private val workManager: WorkManager,
    private val syncRepository: SyncRepository,
    private val localImageStorage: LocalImageStorage
) : BaseRepository(workManager), ProfileRepository {

    override fun getProfileFlow(): Flow<Profile?> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        return profileDao.getProfileFlow(userId).map { it?.toDomain() }
    }

    override suspend fun getProfile(): Result<Profile, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.Error(DataError.Auth.SESSION_EXPIRED)
        
        // Schedule sync
        scheduleSync()
        
        // Try local first
        val localProfile = profileDao.getProfile(userId)?.toDomain()
        
        return if (localProfile != null) {
            Result.Success(localProfile)
        } else {
            // If not local, try fetching from remote
            val remoteResult = profileDataSource.getProfile()
            if (remoteResult is Result.Success) {
                profileDao.insertProfile(remoteResult.data.toEntity(isSynced = true))
            }
            remoteResult
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile, DataError> {
        val profile = Profile(
            id = request.id,
            fullName = request.fullName,
            clinicName = request.clinicName,
            phoneNumber = request.phoneNumber,
            clinicAddress = request.clinicAddress,
            specialization = request.specialization,
            profilePicture = request.profilePicture,
            clinicLogo = request.clinicLogo
        )
        
        profileDao.insertProfile(profile.toEntity(isSynced = false))
        scheduleSync()
        
        return Result.Success(profile)
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: return Result.Error(DataError.Auth.SESSION_EXPIRED)
        val local = profileDao.getProfile(userId)
        
        if (local != null) {
            val updatedProfile = local.toDomain().copy(
                fullName = request.fullName ?: local.fullName,
                clinicName = request.clinicName ?: local.clinicName,
                phoneNumber = request.phoneNumber ?: local.phoneNumber,
                clinicAddress = request.clinicAddress ?: local.clinicAddress,
                specialization = request.specialization ?: local.specialization,
                profilePicture = request.profilePicture ?: local.profilePicture,
                clinicLogo = request.clinicLogo ?: local.clinicLogo
            )
            profileDao.insertProfile(updatedProfile.toEntity(isSynced = false))
            scheduleSync()
            return Result.Success(updatedProfile)
        }
        
        // If not found locally, we can't update easily without the full profile.
        // In this case, we might need to fetch it first.
        val remoteResult = profileDataSource.updateProfile(request)
        if (remoteResult is Result.Success) {
            profileDao.insertProfile(remoteResult.data.toEntity(isSynced = true))
        }
        return remoteResult
    }

    override suspend fun updateProfilePicture(imageUri: Uri, oldImageUrl: String?): Result<Profile, DataError> {
        val remoteResult = profilePictureManager.uploadProfilePicture(imageUri, oldImageUrl)
        
        return when (remoteResult) {
            is Result.Success -> {
                val imageUrl = remoteResult.data
                val updateRequest = UpdateProfileRequest(profilePicture = imageUrl)
                updateProfile(updateRequest)
            }
            is Result.Error -> {
                val localPath = localImageStorage.saveImageLocally(imageUri)
                if (localPath != null) {
                    val updateRequest = UpdateProfileRequest(profilePicture = localPath)
                    updateProfile(updateRequest)
                } else {
                    Result.Error(remoteResult.error, remoteResult.message ?: "Failed to upload profile picture")
                }
            }
            is Result.Loading -> Result.Loading
        }
    }

    override suspend fun updateClinicLogo(imageUri: Uri, oldImageUrl: String?): Result<Profile, DataError> {
        val remoteResult = clinicLogoManager.uploadClinicLogo(imageUri, oldImageUrl)
        
        return when (remoteResult) {
            is Result.Success -> {
                val imageUrl = remoteResult.data
                val updateRequest = UpdateProfileRequest(clinicLogo = imageUrl)
                updateProfile(updateRequest)
            }
            is Result.Error -> {
                val localPath = localImageStorage.saveImageLocally(imageUri)
                if (localPath != null) {
                    val updateRequest = UpdateProfileRequest(clinicLogo = localPath)
                    updateProfile(updateRequest)
                } else {
                    Result.Error(remoteResult.error, remoteResult.message ?: "Failed to upload clinic logo")
                }
            }
            is Result.Loading -> Result.Loading
        }
    }

    override suspend fun syncUnsyncedProfile(): Boolean {
        val unsynced = profileDao.getUnsyncedProfiles()
        for (entity in unsynced) {
            try {
                var profile = entity.toDomain()
                
                // Handle offline images
                var profilePicture = profile.profilePicture
                val currentProfilePicture = profilePicture
                if (currentProfilePicture != null && !currentProfilePicture.startsWith("http")) {
                    val file = File(currentProfilePicture)
                    if (file.exists()) {
                        val uploadResult = profilePictureManager.uploadProfilePicture(Uri.fromFile(file))
                        if (uploadResult is Result.Success) {
                            profilePicture = uploadResult.data
                            localImageStorage.deleteLocalImage(currentProfilePicture)
                        }
                    }
                }

                var clinicLogo = profile.clinicLogo
                val currentClinicLogo = clinicLogo
                if (currentClinicLogo != null && !currentClinicLogo.startsWith("http")) {
                    val file = File(currentClinicLogo)
                    if (file.exists()) {
                        val uploadResult = clinicLogoManager.uploadClinicLogo(Uri.fromFile(file))
                        if (uploadResult is Result.Success) {
                            clinicLogo = uploadResult.data
                            localImageStorage.deleteLocalImage(currentClinicLogo)
                        }
                    }
                }

                profile = profile.copy(profilePicture = profilePicture, clinicLogo = clinicLogo)

                val updateRequest = UpdateProfileRequest(
                    fullName = profile.fullName,
                    clinicName = profile.clinicName,
                    phoneNumber = profile.phoneNumber,
                    clinicAddress = profile.clinicAddress,
                    specialization = profile.specialization,
                    profilePicture = profile.profilePicture,
                    clinicLogo = profile.clinicLogo
                )
                val result = profileDataSource.updateProfile(updateRequest)
                if (result is Result.Success) {
                    profileDao.insertProfile(profile.toEntity(isSynced = true))
                } else {
                    // Try create if update fails (might not have been created yet)
                    val createRequest = CreateProfileRequest(
                        id = profile.id,
                        fullName = profile.fullName ?: "",
                        clinicName = profile.clinicName ?: "",
                        phoneNumber = profile.phoneNumber ?: "",
                        clinicAddress = profile.clinicAddress ?: "",
                        specialization = profile.specialization,
                        profilePicture = profile.profilePicture,
                        clinicLogo = profile.clinicLogo
                    )
                    val createResult = profileDataSource.createProfile(createRequest)
                    if (createResult is Result.Success) {
                        profileDao.insertProfile(profile.toEntity(isSynced = true))
                    }
                }
            } catch (e: Exception) {
                Log.w("ProfileRepo", "Sync upload failed, continuing", e)
            }
        }
        
        // Download flow for profile
        try {
            val remoteResult = profileDataSource.getProfile()
            if (remoteResult is Result.Success) {
                profileDao.insertProfile(remoteResult.data.toEntity(isSynced = true))
            }
        } catch (e: Exception) {
            Log.w("ProfileRepo", "Sync download flow failed", e)
            return false
        }
        
        return true
    }
}
