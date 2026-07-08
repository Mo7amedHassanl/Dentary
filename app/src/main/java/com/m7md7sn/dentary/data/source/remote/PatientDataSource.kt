package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface PatientDataSource {
    suspend fun getAllPatients(): Result<List<Patient>, DataError>
    suspend fun getPatientById(id: String): Result<Patient, DataError>
    suspend fun createPatient(patient: Patient): Result<Patient, DataError>
    suspend fun updatePatient(id: String, patient: Patient): Result<Patient, DataError>
    suspend fun deletePatient(id: String): Result<Unit, DataError>
    suspend fun searchPatients(query: String): Result<List<Patient>, DataError>
    suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>, DataError>
    suspend fun getPatientsUpdatedAfter(timestamp: String): Result<List<Patient>, DataError>
    suspend fun uploadPatientImage(imageUri: android.net.Uri, oldImageUrl: String? = null): Result<String, DataError>
}
