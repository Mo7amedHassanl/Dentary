package com.m7md7sn.dentary.data.repository

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.m7md7sn.dentary.data.sync.SyncWorker

abstract class BaseRepository(
    private val workManager: WorkManager
) {
    protected fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "sync_patients_appointments_profiles",
            androidx.work.ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }
}
