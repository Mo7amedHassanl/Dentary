package com.m7md7sn.dentary.data.repository

import android.net.Uri
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.entity.toDomain
import com.m7md7sn.dentary.data.source.local.entity.toEntity
import com.m7md7sn.dentary.data.source.remote.PatientDataSource
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.domain.model.DataError
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val patientDataSource: PatientDataSource,
    private val patientDao: PatientDao,
    private val auth: Auth
) : PatientRepository {

    override fun getPatientsFlow(): Flow<List<Patient>> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        return patientDao.getPatientsFlow(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAllPatients(): Result<List<Patient>, DataError> {
        val remoteResult = patientDataSource.getAllPatients()
        if (remoteResult is Result.Success) {
            val entities = remoteResult.data.map { it.toEntity() }
            patientDao.insertPatients(entities)
        }
        
        // Return local data as source of truth if remote fails or after remote success
        val userId = auth.currentUserOrNull()?.id ?: ""
        val localPatients = patientDao.getPatients(userId).map { it.toDomain() }
        
        return if (remoteResult is Result.Error && localPatients.isEmpty()) {
            remoteResult
        } else {
            Result.Success(localPatients)
        }
    }

    override suspend fun getPatientById(id: String): Result<Patient, DataError> {
        // Try local first
        val localPatient = patientDao.getPatientById(id)?.toDomain()
        if (localPatient != null) {
            return Result.Success(localPatient)
        }

        // Fallback to remote
        val remoteResult = patientDataSource.getPatientById(id)
        if (remoteResult is Result.Success) {
            patientDao.insertPatient(remoteResult.data.toEntity())
        }
        return remoteResult
    }

    override suspend fun createPatient(patient: Patient): Result<Patient, DataError> {
        // Save to local first (with temporary ID or handle ID generation)
        // For simplicity, we'll try remote first and save to local on success
        // In a full offline sync, we'd save locally with a flag and sync later.
        val remoteResult = patientDataSource.createPatient(patient)
        if (remoteResult is Result.Success) {
            patientDao.insertPatient(remoteResult.data.toEntity())
        }
        return remoteResult
    }

    override suspend fun updatePatient(id: String, patient: Patient): Result<Patient, DataError> {
        val remoteResult = patientDataSource.updatePatient(id, patient)
        if (remoteResult is Result.Success) {
            patientDao.insertPatient(remoteResult.data.toEntity())
        }
        return remoteResult
    }

    override suspend fun deletePatient(id: String): Result<Unit, DataError> {
        val remoteResult = patientDataSource.deletePatient(id)
        if (remoteResult is Result.Success) {
            patientDao.deletePatient(id)
        }
        return remoteResult
    }

    override suspend fun searchPatients(query: String): Result<List<Patient>, DataError> {
        // Search is usually remote-heavy, but we could search local too.
        // For now, let's keep it remote and update local with results.
        val remoteResult = patientDataSource.searchPatients(query)
        if (remoteResult is Result.Success) {
            patientDao.insertPatients(remoteResult.data.map { it.toEntity() })
        }
        return remoteResult
    }

    override suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>, DataError> {
        // Stats are calculated from all patients, so we can calculate them locally
        // or just fetch from remote. Let's keep it remote for now as requested.
        return patientDataSource.getMedicalProcedureStats()
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String, DataError> {
        return patientDataSource.uploadPatientImage(imageUri, oldImageUrl)
    }
}