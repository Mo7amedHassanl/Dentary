package com.m7md7sn.dentary.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m7md7sn.dentary.data.source.local.entity.AttachmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: AttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<AttachmentEntity>)

    @Query("SELECT * FROM attachments WHERE medicalHistoryId = :medicalHistoryId ORDER BY createdAt DESC")
    fun getAttachmentsByMedicalHistoryFlow(medicalHistoryId: String): Flow<List<AttachmentEntity>>

    @Query("SELECT * FROM attachments WHERE medicalHistoryId = :medicalHistoryId ORDER BY createdAt DESC")
    suspend fun getAttachmentsByMedicalHistory(medicalHistoryId: String): List<AttachmentEntity>

    @Query("SELECT * FROM attachments WHERE isSynced = 0")
    suspend fun getUnsyncedAttachments(): List<AttachmentEntity>

    @Query("UPDATE attachments SET isDeletedLocally = 1, isSynced = 0 WHERE medicalHistoryId = :medicalHistoryId")
    suspend fun deleteAttachmentsByMedicalHistory(medicalHistoryId: String)

    @Query("DELETE FROM attachments WHERE id = :id")
    suspend fun hardDeleteAttachment(id: String)

    @Query("DELETE FROM attachments")
    suspend fun deleteAllAttachments()
}
