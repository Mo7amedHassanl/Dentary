package com.m7md7sn.dentary.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m7md7sn.dentary.data.source.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointments(appointments: List<AppointmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY appointmentDate DESC")
    fun getAppointmentsFlow(userId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY appointmentDate DESC")
    suspend fun getAppointments(userId: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: String): AppointmentEntity?

    @Query("SELECT * FROM appointments WHERE patientId = :patientId ORDER BY appointmentDate DESC")
    suspend fun getAppointmentsByPatient(patientId: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE appointmentDate LIKE :date || '%' ORDER BY appointmentDate DESC")
    suspend fun getAppointmentsByDate(date: String): List<AppointmentEntity>

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteAppointment(id: String)

    @Query("DELETE FROM appointments")
    suspend fun deleteAllAppointments()

    @Query("SELECT * FROM appointments WHERE isSynced = 0")
    suspend fun getUnsyncedAppointments(): List<AppointmentEntity>
}