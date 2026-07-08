package com.m7md7sn.dentary.di

import android.content.Context
import androidx.room.Room
import com.m7md7sn.dentary.data.source.local.DentaryDatabase
import com.m7md7sn.dentary.data.source.local.dao.AppointmentDao
import com.m7md7sn.dentary.data.source.local.dao.PatientDao
import com.m7md7sn.dentary.data.source.local.dao.ProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DentaryDatabase {
        return Room.databaseBuilder(
            context,
            DentaryDatabase::class.java,
            DentaryDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePatientDao(database: DentaryDatabase): PatientDao {
        return database.patientDao
    }

    @Provides
    @Singleton
    fun provideAppointmentDao(database: DentaryDatabase): AppointmentDao {
        return database.appointmentDao
    }

    @Provides
    @Singleton
    fun provideProfileDao(database: DentaryDatabase): ProfileDao {
        return database.profileDao
    }
}