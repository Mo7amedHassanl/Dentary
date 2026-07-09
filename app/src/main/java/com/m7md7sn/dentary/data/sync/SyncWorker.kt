package com.m7md7sn.dentary.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.m7md7sn.dentary.data.repository.MedicalHistoryRepository
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.data.repository.AppointmentRepository
import com.m7md7sn.dentary.data.repository.ProfileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val patientRepository: PatientRepository,
    private val appointmentRepository: AppointmentRepository,
    private val profileRepository: ProfileRepository,
    private val medicalHistoryRepository: MedicalHistoryRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val patientSync = async { patientRepository.syncUnsyncedPatients() }
            val appointmentSync = async { appointmentRepository.syncUnsyncedAppointments() }
            val profileSync = async { profileRepository.syncUnsyncedProfile() }
            val medicalHistorySync = async { medicalHistoryRepository.syncUnsyncedMedicalHistories() }

            val results = awaitAll(patientSync, appointmentSync, profileSync, medicalHistorySync)

            if (results.all { it }) {
                androidx.work.ListenableWorker.Result.success()
            } else {
                androidx.work.ListenableWorker.Result.retry()
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed, will retry", e)
            androidx.work.ListenableWorker.Result.retry()
        }
    }
}