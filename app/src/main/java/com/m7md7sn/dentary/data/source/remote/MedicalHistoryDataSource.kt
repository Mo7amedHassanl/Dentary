package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.CreateMedicalHistoryRequest
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface MedicalHistoryDataSource {
    suspend fun createMedicalHistory(request: CreateMedicalHistoryRequest): Result<MedicalHistory, DataError>
    suspend fun getMedicalHistoriesByPatient(patientId: String): Result<List<MedicalHistory>, DataError>
    suspend fun updateMedicalHistory(id: String, description: String): Result<MedicalHistory, DataError>
    suspend fun deleteMedicalHistory(id: String): Result<Unit, DataError>
    suspend fun getMedicalHistoriesUpdatedAfter(timestamp: String): Result<List<MedicalHistory>, DataError>
}
