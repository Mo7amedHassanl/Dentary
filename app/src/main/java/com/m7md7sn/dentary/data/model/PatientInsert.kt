package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientInsert(
    @SerialName("user_id")
    val userId: String,
    @SerialName("name")
    val name: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("age")
    val age: Int? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("medical_history")
    val medicalHistory: String? = null
)
