package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String? = null,
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
    val medicalHistory: String? = null,
    @SerialName("medical_procedure")
    val medicalProcedure: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("last_visit_date")
    val lastVisitDate: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class CreatePatientRequest(
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
    val medicalHistory: String? = null,
    @SerialName("medical_procedure")
    val medicalProcedure: String? = null,
    @SerialName("image")
    val image: String? = null
)

@Serializable
data class UpdatePatientRequest(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val medicalHistory: String? = null,
    val medicalProcedure: String? = null,
    val image: String? = null,
    val lastVisitDate: String? = null
)
