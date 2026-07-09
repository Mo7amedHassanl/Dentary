package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MedicalHistory(
    @SerialName("id")
    val id: String,
    @SerialName("patient_id")
    val patientId: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("attachments")
    val attachments: List<Attachment> = emptyList(),
)

@Serializable
data class CreateMedicalHistoryRequest(
    @SerialName("id")
    val id: String? = null,
    @SerialName("patient_id")
    val patientId: String,
    @SerialName("description")
    val description: String? = null,
)
