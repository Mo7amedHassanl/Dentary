package com.m7md7sn.dentary.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m7md7sn.dentary.data.source.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatients(patients: List<PatientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE userId = :userId ORDER BY name ASC")
    fun getPatientsFlow(userId: String): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE userId = :userId ORDER BY name ASC")
    suspend fun getPatients(userId: String): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getPatientById(id: String): PatientEntity?

    @Query("DELETE FROM patients WHERE id = :id")
    suspend fun deletePatient(id: String)

    @Query("DELETE FROM patients")
    suspend fun deleteAllPatients()

    @Query("SELECT * FROM patients WHERE isSynced = 0")
    suspend fun getUnsyncedPatients(): List<PatientEntity>
}