package com.m7md7sn.dentary.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Appointment(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("patient_id")
    val patientId: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("appointment_date")
    val appointmentDate: String,
    @SerialName("duration_minutes")
    val durationMinutes: Int = 60,
    @SerialName("status")
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    @SerialName("treatment_notes")
    val treatmentNotes: String? = null,
    @SerialName("cost")
    val cost: Double? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
enum class AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
    NO_SHOW
}

@Serializable
data class CreateAppointmentRequest(
    @SerialName("patient_id")
    val patientId: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("appointment_date")
    val appointmentDate: String,
    @SerialName("duration_minutes")
    val durationMinutes: Int = 60
)

@Serializable
data class UpdateAppointmentRequest(
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("appointment_date")
    val appointmentDate: String? = null,
    @SerialName("duration_minutes")
    val durationMinutes: Int? = null,
    @SerialName("status")
    val status: AppointmentStatus? = null,
    @SerialName("treatment_notes")
    val treatmentNotes: String? = null,
    @SerialName("cost")
    val cost: Double? = null
)
