package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePatientRequest(
    val name: String,
    val phoneNumber: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val medicalHistory: String? = null
)

@Serializable
data class UpdatePatientRequest(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val medicalHistory: String? = null,
    val lastVisitDate: String? = null
)
