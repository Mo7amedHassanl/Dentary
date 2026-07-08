package com.m7md7sn.dentary.data.repository

import android.net.Uri
import android.util.Log
import androidx.work.WorkManager
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.entity.toDomain
import com.m7md7sn.dentary.data.source.local.entity.toEntity
import com.m7md7sn.dentary.data.source.remote.PatientDataSource
import com.m7md7sn.dentary.data.sync.SyncWorker
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.data.util.LocalImageStorage
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val patientDataSource: PatientDataSource,
    private val patientDao: PatientDao,
    private val auth: Auth,
    private val workManager: WorkManager,
    private val syncRepository: SyncRepository,
    private val localImageStorage: LocalImageStorage
) : BaseRepository(workManager), PatientRepository {

    companion object {
        private val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override fun getPatientsFlow(): Flow<List<Patient>> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        return patientDao.getPatientsFlow(userId).map { entities ->
            entities.filter { !it.isDeletedLocally }.map { it.toDomain() }
        }
    }

    override suspend fun getAllPatients(): Result<List<Patient>, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        
        // 1. Process any unsynced local changes FIRST (especially deletions)
        syncUnsyncedPatients()

        // 2. Fetch from remote and update local
        try {
            val remoteResult = patientDataSource.getAllPatients()
            if (remoteResult is Result.Success) {
                val entities = remoteResult.data.map { it.toEntity(isSynced = true) }
                patientDao.insertPatients(entities)
                
                // Update sync timestamp to prevent immediate re-sync of all data
                val maxTimestamp = remoteResult.data.mapNotNull { it.updatedAt }.maxOrNull()
                if (maxTimestamp != null) {
                    syncRepository.setLastSyncTimestamp("patients", maxTimestamp)
                }
            }
        } catch (e: Exception) {
            Log.e("PatientRepo", "Remote fetch failed, falling back to local", e)
        }
        
        // Start a background sync for any remaining unsynced local changes
        scheduleSync()
        
        val localPatients = patientDao.getPatients(userId)
            .filter { !it.isDeletedLocally }
            .map { it.toDomain() }
            
        return Result.Success(localPatients)
    }

    override suspend fun getPatientById(id: String): Result<Patient, DataError> {
        val localPatient = patientDao.getPatientById(id)
        if (localPatient != null && !localPatient.isDeletedLocally) {
            return Result.Success(localPatient.toDomain())
        }

        val remoteResult = patientDataSource.getPatientById(id)
        if (remoteResult is Result.Success) {
            patientDao.insertPatient(remoteResult.data.toEntity(isSynced = true))
        }
        return remoteResult
    }

    override suspend fun createPatient(patient: Patient): Result<Patient, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        
        val currentTime = timestampFormat.format(Date())
        
        val newPatient = patient.copy(
            id = if (patient.id.isBlank()) UUID.randomUUID().toString() else patient.id,
            userId = userId,
            createdAt = patient.createdAt ?: currentTime,
            updatedAt = patient.updatedAt ?: currentTime,
            lastVisitDate = patient.lastVisitDate ?: currentTime
        )
        
        patientDao.insertPatient(newPatient.toEntity(isSynced = false))
        scheduleSync()
        
        return Result.Success(newPatient)
    }

    override suspend fun updatePatient(id: String, patient: Patient): Result<Patient, DataError> {
        val currentTime = timestampFormat.format(Date())
        
        val updatedPatient = patient.copy(
            updatedAt = currentTime,
            lastVisitDate = currentTime
        )
        
        patientDao.insertPatient(updatedPatient.toEntity(isSynced = false))
        scheduleSync()
        return Result.Success(updatedPatient)
    }

    override suspend fun deletePatient(id: String): Result<Unit, DataError> {
        val localPatient = patientDao.getPatientById(id)
        if (localPatient != null) {
            patientDao.insertPatient(localPatient.copy(isDeletedLocally = true, isSynced = false))
            scheduleSync()
        }
        return Result.Success(Unit)
    }

    override suspend fun searchPatients(query: String): Result<List<Patient>, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        val localResults = patientDao.getPatients(userId)
            .filter { !it.isDeletedLocally && it.name.contains(query, ignoreCase = true) }
            .map { it.toDomain() }
            
        return Result.Success(localResults)
    }

    override suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        val patients = patientDao.getPatients(userId).filter { !it.isDeletedLocally }
        
        val stats = patients
            .mapNotNull { it.medicalProcedure?.takeIf(String::isNotEmpty) }
            .groupBy { it }
            .map { (procedure, list) -> MedicalProcedureStats(procedure, list.size) }
            
        return Result.Success(stats)
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String, DataError> {
        val remoteResult = patientDataSource.uploadPatientImage(imageUri, oldImageUrl)
        if (remoteResult is Result.Success) {
            return remoteResult
        }
        
        val localPath = localImageStorage.saveImageLocally(imageUri)
        return if (localPath != null) {
            Result.Success(localPath)
        } else {
            Result.Error(DataError.Local.DISK_FULL, "Failed to save image locally")
        }
    }

    override suspend fun syncUnsyncedPatients(): Boolean {
        // 1. Upload Flow
        val unsynced = patientDao.getUnsyncedPatients()
        for (entity in unsynced) {
            try {
                if (entity.isDeletedLocally) {
                    val result = patientDataSource.deletePatient(entity.id)
                    if (result is Result.Success) {
                        patientDao.deletePatient(entity.id)
                    }
                } else {
                    var patient = entity.toDomain()
                    
                    // Handle offline image upload
                    val currentImage = patient.image
                    if (currentImage != null && !currentImage.startsWith("http")) {
                        val imageFile = File(currentImage)
                        if (imageFile.exists()) {
                            val uploadResult = patientDataSource.uploadPatientImage(Uri.fromFile(imageFile))
                            if (uploadResult is Result.Success) {
                                patient = patient.copy(image = uploadResult.data)
                                localImageStorage.deleteLocalImage(currentImage)
                            }
                        }
                    }

                    // Try update, then create if fails
                    val result = patientDataSource.updatePatient(patient.id, patient)
                    if (result is Result.Success) {
                        patientDao.insertPatient(patient.toEntity(isSynced = true))
                    } else {
                        val createResult = patientDataSource.createPatient(patient)
                        if (createResult is Result.Success) {
                            patientDao.insertPatient(patient.toEntity(isSynced = true))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("PatientRepo", "Sync upload failed for entity, continuing", e)
            }
        }

        // 2. Download Flow
        try {
            val lastSync = syncRepository.getLastSyncTimestamp("patients") ?: "1970-01-01T00:00:00Z"
            val remoteResult = patientDataSource.getPatientsUpdatedAfter(lastSync)
            
            if (remoteResult is Result.Success) {
                val remotePatients = remoteResult.data
                if (remotePatients.isNotEmpty()) {
                    val entities = remotePatients.map { it.toEntity(isSynced = true) }
                    patientDao.insertPatients(entities)
                    
                    val maxTimestamp = remotePatients.mapNotNull { it.updatedAt }.maxOrNull()
                    if (maxTimestamp != null) {
                        syncRepository.setLastSyncTimestamp("patients", maxTimestamp)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("PatientRepo", "Sync download flow failed", e)
            return false
        }
        
        return true
    }
}