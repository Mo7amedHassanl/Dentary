package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile, DataError>
    suspend fun createProfile(request: CreateProfileRequest): Result<Profile, DataError>
    suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile, DataError>
    suspend fun updateProfilePicture(imageUri: Uri, oldImageUrl: String? = null): Result<Profile, DataError>
}