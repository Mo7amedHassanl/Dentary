package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.source.remote.PatientDataSource
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val patientDataSource: PatientDataSource
) : PatientRepository {

    override suspend fun getAllPatients(): Result<List<Patient>> {
        return patientDataSource.getAllPatients()
    }

    override suspend fun getPatientById(id: String): Result<Patient> {
        return patientDataSource.getPatientById(id)
    }

    override suspend fun createPatient(patient: Patient): Result<Patient> {
        return patientDataSource.createPatient(patient)
    }

    override suspend fun updatePatient(id: String, patient: Patient): Result<Patient> {
        return patientDataSource.updatePatient(id, patient)
    }

    override suspend fun deletePatient(id: String): Result<Unit> {
        return patientDataSource.deletePatient(id)
    }

    override suspend fun searchPatients(query: String): Result<List<Patient>> {
        return patientDataSource.searchPatients(query)
    }

    override suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>> {
        return patientDataSource.getMedicalProcedureStats()
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String> {
        return patientDataSource.uploadPatientImage(imageUri, oldImageUrl)
    }
}