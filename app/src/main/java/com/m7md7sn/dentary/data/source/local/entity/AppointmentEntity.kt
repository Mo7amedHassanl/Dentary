package com.m7md7sn.dentary.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.AppointmentStatus

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val patientId: String,
    val title: String,
    val description: String?,
    val appointmentDate: String,
    val durationMinutes: Int,
    val status: AppointmentStatus,
    val treatmentNotes: String?,
    val cost: Double?,
    val createdAt: String?,
    val updatedAt: String?,
    val isSynced: Boolean = true,
    val lastUpdatedLocal: Long = System.currentTimeMillis(),
    val isDeletedLocally: Boolean = false
)

fun AppointmentEntity.toDomain(): Appointment {
    return Appointment(
        id = id,
        userId = userId,
        patientId = patientId,
        title = title,
        description = description,
        appointmentDate = appointmentDate,
        durationMinutes = durationMinutes,
        status = status,
        treatmentNotes = treatmentNotes,
        cost = cost,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Appointment.toEntity(
    isSynced: Boolean = true,
    lastUpdatedLocal: Long = System.currentTimeMillis(),
    isDeletedLocally: Boolean = false
): AppointmentEntity {
    return AppointmentEntity(
        id = id,
        userId = userId,
        patientId = patientId,
        title = title,
        description = description,
        appointmentDate = appointmentDate,
        durationMinutes = durationMinutes,
        status = status,
        treatmentNotes = treatmentNotes,
        cost = cost,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        lastUpdatedLocal = lastUpdatedLocal,
        isDeletedLocally = isDeletedLocally
    )
}