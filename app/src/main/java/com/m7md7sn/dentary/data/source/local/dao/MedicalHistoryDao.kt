package com.m7md7sn.dentary.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.m7md7sn.dentary.data.source.local.entity.MedicalHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalHistory(medicalHistory: MedicalHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalHistories(medicalHistories: List<MedicalHistoryEntity>)

    @Query("SELECT * FROM medical_histories WHERE patientId = :patientId AND isDeletedLocally = 0 ORDER BY createdAt DESC")
    fun getMedicalHistoriesByPatientFlow(patientId: String): Flow<List<MedicalHistoryEntity>>

    @Query("SELECT * FROM medical_histories WHERE patientId = :patientId AND isDeletedLocally = 0 ORDER BY createdAt DESC")
    suspend fun getMedicalHistoriesByPatient(patientId: String): List<MedicalHistoryEntity>

    @Query("SELECT * FROM medical_histories WHERE id = :id")
    suspend fun getMedicalHistoryById(id: String): MedicalHistoryEntity?

    @Query("SELECT * FROM medical_histories WHERE isSynced = 0")
    suspend fun getUnsyncedMedicalHistories(): List<MedicalHistoryEntity>

    @Query("UPDATE medical_histories SET isDeletedLocally = 1, isSynced = 0 WHERE id = :id")
    suspend fun deleteMedicalHistory(id: String)

    @Query("DELETE FROM medical_histories WHERE id = :id")
    suspend fun hardDeleteMedicalHistory(id: String)

    @Query("DELETE FROM medical_histories")
    suspend fun deleteAllMedicalHistories()
}
