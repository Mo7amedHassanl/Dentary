package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Appointment(
    val id: String,
    val userId: String,
    val patientId: String,
    val title: String,
    val description: String? = null,
    val appointmentDate: String,
    val durationMinutes: Int = 60,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val treatmentNotes: String? = null,
    val cost: Double? = null,
    val createdAt: String? = null,
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
    val patientId: String,
    val title: String,
    val description: String? = null,
    val appointmentDate: String,
    val durationMinutes: Int = 60
)

@Serializable
data class UpdateAppointmentRequest(
    val title: String? = null,
    val description: String? = null,
    val appointmentDate: String? = null,
    val durationMinutes: Int? = null,
    val status: AppointmentStatus? = null,
    val treatmentNotes: String? = null,
    val cost: Double? = null
)
