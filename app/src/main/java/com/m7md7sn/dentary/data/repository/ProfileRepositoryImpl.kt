package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.data.source.remote.ProfileDataSource
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource
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
}
