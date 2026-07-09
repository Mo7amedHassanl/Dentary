package com.m7md7sn.dentary.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.m7md7sn.dentary.data.source.local.dao.AppointmentDao
import com.m7md7sn.dentary.data.source.local.dao.AttachmentDao
import com.m7md7sn.dentary.data.source.local.dao.MedicalHistoryDao
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.dao.ProfileDao
import com.m7md7sn.dentary.data.source.local.entity.AppointmentEntity
import com.m7md7sn.dentary.data.source.local.entity.AttachmentEntity
import com.m7md7sn.dentary.data.source.local.entity.MedicalHistoryEntity
import com.m7md7sn.dentary.data.source.local.entity.PatientEntity
import com.m7md7sn.dentary.data.source.local.entity.ProfileEntity

@Database(
    entities = [
        PatientEntity::class,
        AppointmentEntity::class,
        ProfileEntity::class,
        MedicalHistoryEntity::class,
        AttachmentEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DentaryDatabase : RoomDatabase() {
    abstract val patientDao: PatientDao
    abstract val appointmentDao: AppointmentDao
    abstract val profileDao: ProfileDao
    abstract val medicalHistoryDao: MedicalHistoryDao
    abstract val attachmentDao: AttachmentDao

    companion object {
        const val DATABASE_NAME = "dentary_db"

        val MIGRATION_3_4 = Migration(3, 4) { db ->
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `medical_histories` (
                    `id` TEXT NOT NULL,
                    `patientId` TEXT NOT NULL,
                    `description` TEXT,
                    `createdAt` TEXT,
                    `updatedAt` TEXT,
                    `isSynced` INTEGER NOT NULL DEFAULT 1,
                    `lastUpdatedLocal` INTEGER NOT NULL,
                    `isDeletedLocally` INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `attachments` (
                    `id` TEXT NOT NULL,
                    `medicalHistoryId` TEXT NOT NULL,
                    `fileName` TEXT NOT NULL,
                    `fileUrl` TEXT NOT NULL,
                    `fileType` TEXT NOT NULL,
                    `description` TEXT,
                    `createdAt` TEXT,
                    `isSynced` INTEGER NOT NULL DEFAULT 1,
                    `lastUpdatedLocal` INTEGER NOT NULL,
                    `isDeletedLocally` INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
        }
    }
}