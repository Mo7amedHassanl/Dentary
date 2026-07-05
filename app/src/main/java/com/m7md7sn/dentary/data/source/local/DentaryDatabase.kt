package com.m7md7sn.dentary.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.entity.PatientEntity

@Database(
    entities = [PatientEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DentaryDatabase : RoomDatabase() {
    abstract val patientDao: PatientDao

    companion object {
        const val DATABASE_NAME = "dentary_db"
    }
}