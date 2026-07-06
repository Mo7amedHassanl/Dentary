package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id")
    val id: String,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("clinic_name")
    val clinicName: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("clinic_address")
    val clinicAddress: String? = null,
    @SerialName("specialization")
    val specialization: String? = null,
    @SerialName("profile_picture")
    val profilePicture: String? = null,
    @SerialName("clinic_logo")
    val clinicLogo: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class CreateProfileRequest(
    @SerialName("id")
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("clinic_name")
    val clinicName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("clinic_address")
    val clinicAddress: String,
    @SerialName("specialization")
    val specialization: String? = null,
    @SerialName("profile_picture")
    val profilePicture: String? = null,
    @SerialName("clinic_logo")
    val clinicLogo: String? = null
)

@Serializable
data class UpdateProfileRequest(
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("clinic_name")
    val clinicName: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("clinic_address")
    val clinicAddress: String? = null,
    @SerialName("specialization")
    val specialization: String? = null,
    @SerialName("profile_picture")
    val profilePicture: String? = null,
    @SerialName("clinic_logo")
    val clinicLogo: String? = null
)