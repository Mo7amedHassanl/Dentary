package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : ProfileDataSource {

    override suspend fun getProfile(): Result<Profile> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val profile = postgrest.from("profiles")
                .select {
                    filter {
                        eq("id", currentUserId)
                    }
                }
                .decodeSingle<Profile>()

            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch profile")
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile> {
        return try {
            val profile = postgrest.from("profiles")
                .insert(request) {
                    select()
                }
                .decodeSingle<Profile>()

            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create profile")
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val profile = postgrest.from("profiles")
                .update(request) {
                    select()
                    filter {
                        eq("id", currentUserId)
                    }
                }
                .decodeSingle<Profile>()

            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update profile")
        }
    }
}
