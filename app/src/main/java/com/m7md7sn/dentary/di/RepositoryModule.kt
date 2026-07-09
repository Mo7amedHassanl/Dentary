package com.m7md7sn.dentary.di

import com.m7md7sn.dentary.data.repository.AppointmentRepository
import com.m7md7sn.dentary.data.repository.AppointmentRepositoryImpl
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.AuthRepositoryImpl
import com.m7md7sn.dentary.data.repository.MedicalHistoryRepository
import com.m7md7sn.dentary.data.repository.MedicalHistoryRepositoryImpl
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.data.repository.PatientRepositoryImpl
import com.m7md7sn.dentary.data.repository.ProfileRepository
import com.m7md7sn.dentary.data.repository.ProfileRepositoryImpl
import com.m7md7sn.dentary.data.source.remote.AppointmentDataSource
import com.m7md7sn.dentary.data.source.remote.AppointmentDataSourceImpl
import com.m7md7sn.dentary.data.source.remote.AttachmentDataSource
import com.m7md7sn.dentary.data.source.remote.AttachmentDataSourceImpl
import com.m7md7sn.dentary.data.source.remote.AuthDataSource
import com.m7md7sn.dentary.data.source.remote.AuthDataSourceImpl
import com.m7md7sn.dentary.data.source.remote.MedicalHistoryDataSource
import com.m7md7sn.dentary.data.source.remote.MedicalHistoryDataSourceImpl
import com.m7md7sn.dentary.data.source.remote.PatientDataSource
import com.m7md7sn.dentary.data.source.remote.PatientDataSourceImpl
import com.m7md7sn.dentary.data.source.remote.ProfileDataSource
import com.m7md7sn.dentary.data.source.remote.ProfileDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPatientDataSource(
        patientDataSourceImpl: PatientDataSourceImpl
    ): PatientDataSource

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        patientRepositoryImpl: PatientRepositoryImpl
    ): PatientRepository

    @Binds
    @Singleton
    abstract fun bindProfileDataSource(
        profileDataSourceImpl: ProfileDataSourceImpl
    ): ProfileDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindAppointmentDataSource(
        appointmentDataSourceImpl: AppointmentDataSourceImpl
    ): AppointmentDataSource

    @Binds
    @Singleton
    abstract fun bindAppointmentRepository(
        appointmentRepositoryImpl: AppointmentRepositoryImpl
    ): AppointmentRepository

    @Binds
    @Singleton
    abstract fun bindMedicalHistoryDataSource(
        medicalHistoryDataSourceImpl: MedicalHistoryDataSourceImpl
    ): MedicalHistoryDataSource

    @Binds
    @Singleton
    abstract fun bindAttachmentDataSource(
        attachmentDataSourceImpl: AttachmentDataSourceImpl
    ): AttachmentDataSource

    @Binds
    @Singleton
    abstract fun bindMedicalHistoryRepository(
        medicalHistoryRepositoryImpl: MedicalHistoryRepositoryImpl
    ): MedicalHistoryRepository
}