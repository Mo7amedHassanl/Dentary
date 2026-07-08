package com.m7md7sn.dentary.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.m7md7sn.dentary.data.source.local.dao.AppointmentDao
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.dao.ProfileDao
import com.m7md7sn.dentary.data.source.local.entity.AppointmentEntity
import com.m7md7sn.dentary.data.source.local.entity.PatientEntity
import com.m7md7sn.dentary.data.source.local.entity.ProfileEntity

@Database(
    entities = [
        PatientEntity::class,
        AppointmentEntity::class,
        ProfileEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DentaryDatabase : RoomDatabase() {
    abstract val patientDao: PatientDao
    abstract val appointmentDao: AppointmentDao
    abstract val profileDao: ProfileDao

    companion object {
        const val DATABASE_NAME = "dentary_db"
    }
}