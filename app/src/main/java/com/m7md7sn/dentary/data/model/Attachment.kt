package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    @SerialName("id")
    val id: String,
    @SerialName("medical_history_id")
    val medicalHistoryId: String,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_url")
    val fileUrl: String,
    @SerialName("file_type")
    val fileType: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
)

@Serializable
data class CreateAttachmentRequest(
    @SerialName("id")
    val id: String? = null,
    @SerialName("medical_history_id")
    val medicalHistoryId: String,
    @SerialName("file_name")
    val fileName: String,
    @SerialName("file_url")
    val fileUrl: String,
    @SerialName("file_type")
    val fileType: String,
    @SerialName("description")
    val description: String? = null,
)
