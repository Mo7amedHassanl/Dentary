package com.m7md7sn.dentary.data.session

import android.content.Context
import android.content.SharedPreferences
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

class SharedPreferencesSessionManager(
    context: Context
) : SessionManager {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun saveSession(session: UserSession) {
        try {
            val sessionJson = json.encodeToString(session)
            sharedPreferences.edit().apply {
                putString(KEY_SESSION, sessionJson)
                apply()
            }
        } catch (e: Exception) {
            deleteSession()
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun loadSession(): UserSession? {
        return try {
            val sessionJson = sharedPreferences.getString(KEY_SESSION, null) ?: return null
            json.decodeFromString<UserSession>(sessionJson)
        } catch (e: Exception) {
            deleteSession()
            null
        }
    }

    override suspend fun deleteSession() {
        sharedPreferences.edit().apply {
            remove(KEY_SESSION)
            // Legacy keys cleanup
            remove("access_token")
            remove("refresh_token")
            remove("expires_in")
            remove("token_type")
            apply()
        }
    }

    companion object {
        private const val KEY_SESSION = "user_session"
    }
}
