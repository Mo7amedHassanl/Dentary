package com.m7md7sn.dentary.data.repository

import android.net.Uri
import android.util.Log
import androidx.work.WorkManager
import com.m7md7sn.dentary.data.model.Attachment
import com.m7md7sn.dentary.data.model.CreateAttachmentRequest
import com.m7md7sn.dentary.data.model.CreateMedicalHistoryRequest
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.data.source.local.dao.AttachmentDao
import com.m7md7sn.dentary.data.source.local.dao.MedicalHistoryDao
import com.m7md7sn.dentary.data.source.local.entity.toDomain
import com.m7md7sn.dentary.data.source.local.entity.toEntity
import com.m7md7sn.dentary.data.source.remote.AttachmentDataSource
import com.m7md7sn.dentary.data.source.remote.MedicalHistoryDataSource
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.domain.model.DataError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MedicalHistoryRepositoryImpl @Inject constructor(
    private val medicalHistoryDataSource: MedicalHistoryDataSource,
    private val attachmentDataSource: AttachmentDataSource,
    private val medicalHistoryDao: MedicalHistoryDao,
    private val attachmentDao: AttachmentDao,
    private val workManager: WorkManager,
    private val syncRepository: SyncRepository
) : BaseRepository(workManager), MedicalHistoryRepository {

    companion object {
        private val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override fun getMedicalHistoriesByPatientFlow(patientId: String): Flow<List<MedicalHistory>> {
        return medicalHistoryDao.getMedicalHistoriesByPatientFlow(patientId).flatMapLatest { entities ->
            val nonDeleted = entities.filter { !it.isDeletedLocally }
            if (nonDeleted.isEmpty()) {
                flowOf(emptyList())
            } else {
                val attachmentFlows = nonDeleted.map { entity ->
                    attachmentDao.getAttachmentsByMedicalHistoryFlow(entity.id).map { attachments ->
                        entity.toDomain().copy(
                            attachments = attachments.map { it.toDomain() }
                        )
                    }
                }
                combine(attachmentFlows) { histories -> histories.toList() }
            }
        }
    }

    override suspend fun saveMedicalHistory(
        patientId: String,
        description: String,
        attachments: List<AttachmentCandidate>
    ): Result<MedicalHistory, DataError> {
        val currentTime = timestampFormat.format(Date())

        val medicalHistory = MedicalHistory(
            id = UUID.randomUUID().toString(),
            patientId = patientId,
            description = description.ifBlank { null },
            createdAt = currentTime,
            updatedAt = currentTime
        )

        medicalHistoryDao.insertMedicalHistory(
            medicalHistory.toEntity(isSynced = false)
        )

        for (attachment in attachments) {
            val attachmentEntity = com.m7md7sn.dentary.data.source.local.entity.AttachmentEntity(
                id = UUID.randomUUID().toString(),
                medicalHistoryId = medicalHistory.id,
                fileName = attachment.fileName,
                fileUrl = attachment.uri.toString(),
                fileType = attachment.mimeType,
                description = attachment.description.ifBlank { null },
                createdAt = currentTime,
                isSynced = false
            )
            attachmentDao.insertAttachment(attachmentEntity)
        }

        scheduleSync()

        return Result.Success(medicalHistory)
    }

    override suspend fun deleteMedicalHistory(id: String): Result<Unit, DataError> {
        return try {
            medicalHistoryDao.deleteMedicalHistory(id)
            attachmentDao.deleteAttachmentsByMedicalHistory(id)
            scheduleSync()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN, e.message)
        }
    }

    override suspend fun syncUnsyncedMedicalHistories(): Boolean {
        val unsyncedHistories = medicalHistoryDao.getUnsyncedMedicalHistories()
        for (entity in unsyncedHistories) {
            try {
                if (entity.isDeletedLocally) {
                    medicalHistoryDataSource.deleteMedicalHistory(entity.id)
                    medicalHistoryDao.hardDeleteMedicalHistory(entity.id)
                    attachmentDao.deleteAttachmentsByMedicalHistory(entity.id)
                } else {
                    val request = CreateMedicalHistoryRequest(
                        id = entity.id,
                        patientId = entity.patientId,
                        description = entity.description
                    )
                    val result = medicalHistoryDataSource.createMedicalHistory(request)
                    if (result is Result.Success) {
                        medicalHistoryDao.insertMedicalHistory(
                            entity.copy(isSynced = true)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w("MedicalHistoryRepo", "Sync upload failed for medical history", e)
            }
        }

        val unsyncedAttachments = attachmentDao.getUnsyncedAttachments()
        for (entity in unsyncedAttachments) {
            try {
                if (entity.isDeletedLocally) {
                    attachmentDataSource.deleteAttachment(entity.id)
                    attachmentDao.hardDeleteAttachment(entity.id)
                } else {
                    val fileUri = Uri.parse(entity.fileUrl)
                    val uploadResult = attachmentDataSource.uploadAttachmentFile(fileUri, entity.medicalHistoryId)
                    if (uploadResult is Result.Success) {
                        val request = CreateAttachmentRequest(
                            id = entity.id,
                            medicalHistoryId = entity.medicalHistoryId,
                            fileName = entity.fileName,
                            fileUrl = uploadResult.data,
                            fileType = entity.fileType,
                            description = entity.description
                        )
                        val createResult = attachmentDataSource.createAttachment(request)
                        if (createResult is Result.Success) {
                            attachmentDao.insertAttachment(
                                entity.copy(fileUrl = uploadResult.data, isSynced = true)
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("MedicalHistoryRepo", "Sync upload failed for attachment", e)
            }
        }

        try {
            val lastSync = syncRepository.getLastSyncTimestamp("medical_histories") ?: "1970-01-01T00:00:00Z"
            val remoteResult = medicalHistoryDataSource.getMedicalHistoriesUpdatedAfter(lastSync)
            if (remoteResult is Result.Success) {
                val remoteHistories = remoteResult.data
                if (remoteHistories.isNotEmpty()) {
                    val entities = remoteHistories.map { it.toEntity(isSynced = true) }
                    medicalHistoryDao.insertMedicalHistories(entities)

                    val maxTimestamp = remoteHistories.mapNotNull { it.updatedAt }.maxOrNull()
                    if (maxTimestamp != null) {
                        syncRepository.setLastSyncTimestamp("medical_histories", maxTimestamp)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("MedicalHistoryRepo", "Sync download flow failed", e)
            return false
        }

        return true
    }

    override suspend fun getAllMedicalHistoriesForPatient(patientId: String): Result<List<MedicalHistory>, DataError> {
        syncUnsyncedMedicalHistories()

        try {
            val remoteResult = medicalHistoryDataSource.getMedicalHistoriesByPatient(patientId)
            if (remoteResult is Result.Success) {
                val entities = remoteResult.data.map { it.toEntity(isSynced = true) }
                medicalHistoryDao.insertMedicalHistories(entities)
            }
        } catch (e: Exception) {
            Log.e("MedicalHistoryRepo", "Remote fetch failed, falling back to local", e)
        }

        scheduleSync()

        val localHistories = medicalHistoryDao.getMedicalHistoriesByPatient(patientId)
            .filter { !it.isDeletedLocally }
            .map { it.toDomain() }

        return Result.Success(localHistories)
    }
}
