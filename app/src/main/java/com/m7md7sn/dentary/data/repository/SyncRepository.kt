package com.m7md7sn.dentary.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)

    fun getLastSyncTimestamp(entity: String): String? {
        return prefs.getString("last_sync_$entity", null)
    }

    fun setLastSyncTimestamp(entity: String, timestamp: String) {
        prefs.edit().putString("last_sync_$entity", timestamp).apply()
    }

    fun clearSyncMetadata() {
        prefs.edit().clear().apply()
    }
}
