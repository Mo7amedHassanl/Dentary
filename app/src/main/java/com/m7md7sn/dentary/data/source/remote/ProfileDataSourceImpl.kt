package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.CreateProfileRequest
import com.m7md7sn.dentary.data.model.UpdateProfileRequest
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class ProfileDataSourceImpl @Inject constructor(
    private val auth: Auth
) : ProfileDataSource {

    @OptIn(ExperimentalTime::class)
    override suspend fun getProfile(): Result<Profile> {
        return try {
            val currentUser = auth.currentUserOrNull()
                ?: return Result.Error("User not authenticated")

            val userMetadata = currentUser.userMetadata

            val fullName = userMetadata?.get("display_name")?.jsonPrimitive?.content
                ?: userMetadata?.get("username")?.jsonPrimitive?.content

            val profile = Profile(
                id = currentUser.id,
                fullName = fullName,
                clinicName = userMetadata?.get("clinic_name")?.jsonPrimitive?.content,
                phoneNumber = userMetadata?.get("phone_number")?.jsonPrimitive?.content,
                clinicAddress = userMetadata?.get("address")?.jsonPrimitive?.content,
                specialization = userMetadata?.get("specialization")?.jsonPrimitive?.content,
                profilePicture = userMetadata?.get("profile_picture")?.jsonPrimitive?.content,
                createdAt = currentUser.createdAt.toString(),
                updatedAt = currentUser.lastSignInAt.toString()
            )

            Result.Success(profile)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch profile from user metadata")
        }
    }

    override suspend fun createProfile(request: CreateProfileRequest): Result<Profile> {
        return try {
            // For creation, we update the user metadata instead of creating a database record
            val metadataJson = buildJsonObject {
                put("display_name", JsonPrimitive(request.fullName))
                put("username", JsonPrimitive(request.fullName))
                put("clinic_name", JsonPrimitive(request.clinicName))
                put("phone_number", JsonPrimitive(request.phoneNumber))
                put("address", JsonPrimitive(request.clinicAddress))
                request.specialization?.let { put("specialization", JsonPrimitive(it)) }
                request.profilePicture?.let { put("profile_picture", JsonPrimitive(it)) }
            }

            auth.updateUser {
                data = metadataJson
            }

            // Return the updated profile
            getProfile()
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create profile in user metadata")
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Result<Profile> {
        return try {
            // Build update data, only including non-null values
            val metadataJson = buildJsonObject {
                request.fullName?.let {
                    put("display_name", JsonPrimitive(it))
                    put("username", JsonPrimitive(it))
                }
                request.clinicName?.let { put("clinic_name", JsonPrimitive(it)) }
                request.phoneNumber?.let { put("phone_number", JsonPrimitive(it)) }
                request.clinicAddress?.let { put("address", JsonPrimitive(it)) }
                request.specialization?.let { put("specialization", JsonPrimitive(it)) }
                request.profilePicture?.let { put("profile_picture", JsonPrimitive(it)) }
            }

            auth.updateUser {
                data = metadataJson
            }

            // Return the updated profile
            getProfile()
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update profile in user metadata")
        }
    }
}