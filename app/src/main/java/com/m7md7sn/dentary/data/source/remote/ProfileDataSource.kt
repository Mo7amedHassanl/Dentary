package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface ProfileDataSource {
    suspend fun getProfile(): Result<Profile, DataError>
    suspend fun createProfile(request: CreateProfileRequest): Result<Profile, DataError>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile, DataError>
}
