package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val fullName: String? = null,
    val clinicName: String? = null,
    val phoneNumber: String? = null,
    val clinicAddress: String? = null,
    val specialization: String? = null,
    val profilePicture: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class CreateProfileRequest(
    val id: String,
    val fullName: String,
    val clinicName: String,
    val phoneNumber: String,
    val clinicAddress: String,
    val specialization: String? = null,
    val profilePicture: String? = null
)

@Serializable
data class UpdateProfileRequest(
    val fullName: String? = null,
    val clinicName: String? = null,
    val phoneNumber: String? = null,
    val clinicAddress: String? = null,
    val specialization: String? = null,
    val profilePicture: String? = null
)