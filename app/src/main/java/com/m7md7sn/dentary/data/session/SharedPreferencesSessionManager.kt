package com.m7md7sn.dentary.data.session

import android.content.Context
import android.content.SharedPreferences
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class SharedPreferencesSessionManager(
    private val context: Context
) : SessionManager {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)

    private val _sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.NotAuthenticated())
    val sessionStatus: StateFlow<SessionStatus> = _sessionStatus.asStateFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun saveSession(session: UserSession) {
        _sessionStatus.value = SessionStatus.Authenticated(session = session)

        try {
            // Create a simple data structure that can be serialized
            val sessionMap = mapOf(
                "accessToken" to session.accessToken,
                "refreshToken" to session.refreshToken,
                "expiresAt" to session.expiresAt.epochSeconds.toString(),
                "expiresIn" to session.expiresIn.toString(),
                "tokenType" to (session.tokenType ?: "Bearer"),
                "userEmail" to (session.user?.email ?: ""),
                "userId" to (session.user?.id ?: "")
            )

            val sessionString = json.encodeToString(sessionMap)
            sharedPreferences.edit()
                .putString(SESSION_KEY, sessionString)
                .apply()
        } catch (e: Exception) {
            // If serialization fails, remove any existing session
            sharedPreferences.edit().remove(SESSION_KEY).apply()
        }
    }

    override suspend fun loadSession(): UserSession? {
        val sessionString = sharedPreferences.getString(SESSION_KEY, null)
        return try {
            sessionString?.let {
                val sessionMap = json.decodeFromString<Map<String, String>>(it)

                // Reconstruct UserSession from the stored data
                val userSession = UserSession(
                    accessToken = sessionMap["accessToken"] ?: "",
                    refreshToken = sessionMap["refreshToken"] ?: "",
                    expiresAt = Instant.fromEpochSeconds(sessionMap["expiresAt"]?.toLongOrNull() ?: 0),
                    expiresIn = sessionMap["expiresIn"]?.toLongOrNull() ?: 0,
                    tokenType = sessionMap["tokenType"] ?: "Bearer",
                    user = null // We'll let Supabase retrieve the user info
                )
                userSession
            }
        } catch (e: Exception) {
            // Clear corrupted session
            sharedPreferences.edit().remove(SESSION_KEY).apply()
            null
        }
    }

    override suspend fun deleteSession() {
        _sessionStatus.value = SessionStatus.NotAuthenticated()
        sharedPreferences.edit().remove(SESSION_KEY).apply()
    }

    companion object {
        private const val SESSION_KEY = "user_session"
    }
}
