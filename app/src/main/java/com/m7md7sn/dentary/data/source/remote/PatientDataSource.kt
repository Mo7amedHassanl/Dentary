package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.utils.Result

interface PatientDataSource {
    suspend fun getAllPatients(): Result<List<Patient>>
    suspend fun getPatientById(id: String): Result<Patient>
    suspend fun createPatient(patient: Patient): Result<Patient>
    suspend fun updatePatient(id: String, patient: Patient): Result<Patient>
    suspend fun deletePatient(id: String): Result<Unit>
    suspend fun searchPatients(query: String): Result<List<Patient>>
}
