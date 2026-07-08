package com.m7md7sn.dentary.data.repository

import android.util.Log
import androidx.work.WorkManager
import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.data.source.local.dao.AppointmentDao
import com.m7md7sn.dentary.data.source.local.entity.toDomain
import com.m7md7sn.dentary.data.source.local.entity.toEntity
import com.m7md7sn.dentary.data.source.remote.AppointmentDataSource
import com.m7md7sn.dentary.data.sync.SyncWorker
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDataSource: AppointmentDataSource,
    private val appointmentDao: AppointmentDao,
    private val auth: Auth,
    private val workManager: WorkManager,
    private val syncRepository: SyncRepository
) : BaseRepository(workManager), AppointmentRepository {

    override fun getAppointmentsFlow(): Flow<List<Appointment>> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        return appointmentDao.getAppointmentsFlow(userId).map { entities ->
            entities.filter { !it.isDeletedLocally }.map { it.toDomain() }
        }
    }

    override suspend fun getAllAppointments(): Result<List<Appointment>, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        
        // 1. Process any unsynced local changes FIRST
        syncUnsyncedAppointments()

        // 2. Fetch from remote
        try {
            val remoteResult = appointmentDataSource.getAllAppointments()
            if (remoteResult is Result.Success) {
                val entities = remoteResult.data.map { it.toEntity(isSynced = true) }
                appointmentDao.insertAppointments(entities)
                
                val maxTimestamp = remoteResult.data.mapNotNull { it.updatedAt }.maxOrNull()
                if (maxTimestamp != null) {
                    syncRepository.setLastSyncTimestamp("appointments", maxTimestamp)
                }
            }
        } catch (e: Exception) {
            Log.e("AppointmentRepo", "Remote fetch failed, falling back to local", e)
        }
        
        scheduleSync()

        val localAppointments = appointmentDao.getAppointments(userId)
            .filter { !it.isDeletedLocally }
            .map { it.toDomain() }
            
        return Result.Success(localAppointments)
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment, DataError> {
        val local = appointmentDao.getAppointmentById(id)
        if (local != null && !local.isDeletedLocally) {
            return Result.Success(local.toDomain())
        }

        val remoteResult = appointmentDataSource.getAppointmentById(id)
        if (remoteResult is Result.Success) {
            appointmentDao.insertAppointment(remoteResult.data.toEntity(isSynced = true))
        }
        return remoteResult
    }

    override suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>, DataError> {
        val local = appointmentDao.getAppointmentsByPatient(patientId)
            .filter { !it.isDeletedLocally }
            .map { it.toDomain() }
        return Result.Success(local)
    }

    override suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>, DataError> {
        val local = appointmentDao.getAppointmentsByDate(date)
            .filter { !it.isDeletedLocally }
            .map { it.toDomain() }
        return Result.Success(local)
    }

    override suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment, DataError> {
        val userId = auth.currentUserOrNull()?.id ?: ""
        val appointment = Appointment(
            id = UUID.randomUUID().toString(),
            userId = userId,
            patientId = request.patientId,
            title = request.title,
            description = request.description,
            appointmentDate = request.appointmentDate,
            durationMinutes = request.durationMinutes
        )
        
        appointmentDao.insertAppointment(appointment.toEntity(isSynced = false))
        scheduleSync()
        
        return Result.Success(appointment)
    }

    override suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment, DataError> {
        val local = appointmentDao.getAppointmentById(id)
        if (local != null) {
            val updatedAppointment = local.toDomain().copy(
                title = request.title ?: local.title,
                description = request.description ?: local.description,
                appointmentDate = request.appointmentDate ?: local.appointmentDate,
                durationMinutes = request.durationMinutes ?: local.durationMinutes,
                status = request.status ?: local.status,
                treatmentNotes = request.treatmentNotes ?: local.treatmentNotes,
                cost = request.cost ?: local.cost
            )
            appointmentDao.insertAppointment(updatedAppointment.toEntity(isSynced = false))
            scheduleSync()
            return Result.Success(updatedAppointment)
        }
        return Result.Error(DataError.Local.UNKNOWN, "Appointment not found locally")
    }

    override suspend fun deleteAppointment(id: String): Result<Unit, DataError> {
        val local = appointmentDao.getAppointmentById(id)
        if (local != null) {
            appointmentDao.insertAppointment(local.copy(isDeletedLocally = true, isSynced = false))
            scheduleSync()
        }
        return Result.Success(Unit)
    }

    override suspend fun syncUnsyncedAppointments(): Boolean {
        // 1. Upload Flow
        val unsynced = appointmentDao.getUnsyncedAppointments()
        for (entity in unsynced) {
            try {
                if (entity.isDeletedLocally) {
                    val result = appointmentDataSource.deleteAppointment(entity.id)
                    if (result is Result.Success) {
                        appointmentDao.deleteAppointment(entity.id)
                    }
                } else {
                    val appointment = entity.toDomain()
                    val updateRequest = UpdateAppointmentRequest(
                        title = appointment.title,
                        description = appointment.description,
                        appointmentDate = appointment.appointmentDate,
                        durationMinutes = appointment.durationMinutes,
                        status = appointment.status,
                        treatmentNotes = appointment.treatmentNotes,
                        cost = appointment.cost
                    )
                    val result = appointmentDataSource.updateAppointment(appointment.id, updateRequest)
                    if (result is Result.Success) {
                        appointmentDao.insertAppointment(entity.copy(isSynced = true))
                    } else {
                        val createRequest = CreateAppointmentRequest(
                            patientId = appointment.patientId,
                            title = appointment.title,
                            description = appointment.description,
                            appointmentDate = appointment.appointmentDate,
                            durationMinutes = appointment.durationMinutes
                        )
                        val createResult = appointmentDataSource.createAppointment(createRequest)
                        if (createResult is Result.Success) {
                             appointmentDao.insertAppointment(entity.copy(isSynced = true))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w("AppointmentRepo", "Sync upload failed for entity, continuing", e)
            }
        }

        // 2. Download Flow
        try {
            val lastSync = syncRepository.getLastSyncTimestamp("appointments") ?: "1970-01-01T00:00:00Z"
            val remoteResult = appointmentDataSource.getAppointmentsUpdatedAfter(lastSync)
            
            if (remoteResult is Result.Success) {
                val remoteAppointments = remoteResult.data
                if (remoteAppointments.isNotEmpty()) {
                    val entities = remoteAppointments.map { it.toEntity(isSynced = true) }
                    appointmentDao.insertAppointments(entities)
                    
                    val maxTimestamp = remoteAppointments.mapNotNull { it.updatedAt }.maxOrNull()
                    if (maxTimestamp != null) {
                        syncRepository.setLastSyncTimestamp("appointments", maxTimestamp)
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("AppointmentRepo", "Sync download flow failed", e)
            return false
        }
        
        return true
    }
}