package com.m7md7sn.dentary.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m7md7sn.dentary.data.model.Attachment

@Entity(tableName = "attachments")
data class AttachmentEntity(
    @PrimaryKey
    val id: String,
    val medicalHistoryId: String,
    val fileName: String,
    val fileUrl: String,
    val fileType: String,
    val description: String?,
    val createdAt: String?,
    val isSynced: Boolean = true,
    val lastUpdatedLocal: Long = System.currentTimeMillis(),
    val isDeletedLocally: Boolean = false
)

fun AttachmentEntity.toDomain(): Attachment {
    return Attachment(
        id = id,
        medicalHistoryId = medicalHistoryId,
        fileName = fileName,
        fileUrl = fileUrl,
        fileType = fileType,
        description = description,
        createdAt = createdAt
    )
}

fun Attachment.toEntity(
    isSynced: Boolean = true,
    lastUpdatedLocal: Long = System.currentTimeMillis(),
    isDeletedLocally: Boolean = false
): AttachmentEntity {
    return AttachmentEntity(
        id = id,
        medicalHistoryId = medicalHistoryId,
        fileName = fileName,
        fileUrl = fileUrl,
        fileType = fileType,
        description = description,
        createdAt = createdAt,
        isSynced = isSynced,
        lastUpdatedLocal = lastUpdatedLocal,
        isDeletedLocally = isDeletedLocally
    )
}
