package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : ProfileDataSource {

    override suspend fun getProfile(): Result<Profile, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val profile = postgrest
                .from("profiles")
                .select {
                    filter {
                        eq("id", currentUserId)
                    }
                }
                .decodeSingle<Profile>()

            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to fetch profile")
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile, DataError> {
        return try {
            val createdProfile = postgrest
                .from("profiles")
                .insert(request) {
                    select()
                }
                .decodeSingle<Profile>()

            Result.Success(createdProfile)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to create profile")
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val updatedProfile = postgrest
                .from("profiles")
                .update(request) {
                    select()
                    filter {
                        eq("id", currentUserId)
                    }
                }
                .decodeSingle<Profile>()

            Result.Success(updatedProfile)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to update profile")
        }
    }
}