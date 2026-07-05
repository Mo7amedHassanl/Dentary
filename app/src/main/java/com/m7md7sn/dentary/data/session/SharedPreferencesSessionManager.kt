package com.m7md7sn.dentary.data.session

import android.content.Context
import android.content.SharedPreferences
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlin.time.ExperimentalTime

class SharedPreferencesSessionManager(
    context: Context
) : SessionManager {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)

    @OptIn(ExperimentalTime::class)
    override suspend fun saveSession(session: UserSession) {
        try {
            sharedPreferences.edit().apply {
                putString(KEY_ACCESS_TOKEN, session.accessToken)
                putString(KEY_REFRESH_TOKEN, session.refreshToken)
                putLong(KEY_EXPIRES_IN, session.expiresIn)
                putString(KEY_TOKEN_TYPE, session.tokenType)
                apply()
            }
        } catch (e: Exception) {
            deleteSession()
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun loadSession(): UserSession? {
        return try {
            val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null) ?: return null
            val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null) ?: return null
            val expiresIn = sharedPreferences.getLong(KEY_EXPIRES_IN, 0)
            val tokenType = sharedPreferences.getString(KEY_TOKEN_TYPE, "Bearer") ?: "Bearer"

            UserSession(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn,
                tokenType = tokenType,
                user = null
            )
        } catch (e: Exception) {
            deleteSession()
            null
        }
    }

    override suspend fun deleteSession() {
        sharedPreferences.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_EXPIRES_IN)
            remove(KEY_TOKEN_TYPE)
            apply()
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_IN = "expires_in"
        private const val KEY_TOKEN_TYPE = "token_type"
    }
}