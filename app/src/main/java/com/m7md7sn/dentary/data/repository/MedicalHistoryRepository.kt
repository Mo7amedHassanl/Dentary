package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import kotlinx.coroutines.flow.Flow

data class AttachmentCandidate(
    val uri: Uri,
    val fileName: String,
    val description: String = "",
    val mimeType: String
)

interface MedicalHistoryRepository {
    fun getMedicalHistoriesByPatientFlow(patientId: String): Flow<List<MedicalHistory>>
    suspend fun saveMedicalHistory(
        patientId: String,
        description: String,
        attachments: List<AttachmentCandidate>
    ): Result<MedicalHistory, DataError>
    suspend fun deleteMedicalHistory(id: String): Result<Unit, DataError>
    suspend fun syncUnsyncedMedicalHistories(): Boolean
    suspend fun getAllMedicalHistoriesForPatient(patientId: String): Result<List<MedicalHistory>, DataError>
}
