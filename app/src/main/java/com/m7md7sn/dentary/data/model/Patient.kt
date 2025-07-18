package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val id: String,
    val userId: String? = null,
    val name: String,
    val phoneNumber: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val medicalHistory: String? = null,
    val lastVisitDate: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
