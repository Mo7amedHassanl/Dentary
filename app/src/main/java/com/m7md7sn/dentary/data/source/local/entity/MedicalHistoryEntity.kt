package com.m7md7sn.dentary.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m7md7sn.dentary.data.model.MedicalHistory

@Entity(tableName = "medical_histories")
data class MedicalHistoryEntity(
    @PrimaryKey
    val id: String,
    val patientId: String,
    val description: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val isSynced: Boolean = true,
    val lastUpdatedLocal: Long = System.currentTimeMillis(),
    val isDeletedLocally: Boolean = false
)

fun MedicalHistoryEntity.toDomain(): MedicalHistory {
    return MedicalHistory(
        id = id,
        patientId = patientId,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MedicalHistory.toEntity(
    isSynced: Boolean = true,
    lastUpdatedLocal: Long = System.currentTimeMillis(),
    isDeletedLocally: Boolean = false
): MedicalHistoryEntity {
    return MedicalHistoryEntity(
        id = id,
        patientId = patientId,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced,
        lastUpdatedLocal = lastUpdatedLocal,
        isDeletedLocally = isDeletedLocally
    )
}
