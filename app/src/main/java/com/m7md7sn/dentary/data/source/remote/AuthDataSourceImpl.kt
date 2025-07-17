package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val auth: Auth
) : AuthDataSource {
    override suspend fun getCurrentUser(): UserInfo? {
        return try {
            // First try to get current user (this should trigger session loading)
            auth.retrieveUserForCurrentSession(updateSession = true)
        } catch (e: Exception) {
            try {
                auth.refreshCurrentSession()
                auth.currentUserOrNull()
            } catch (refreshException: Exception) {
                null
            }
        }
    }

    override suspend fun login(credentials: LoginCredentials): Result<UserInfo> {
        return try {
            auth.signInWith(Email) {
                email = credentials.email
                password = credentials.password
            }

            // Verify the user is properly authenticated
            val userInfo = auth.currentUserOrNull()
            if (userInfo != null) {
                Result.Success(userInfo)
            } else {
                Result.Error("Authentication failed - user not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo> {
        return try {
            val userSession = auth.signUpWith(Email) {
                email = credentials.email
                password = credentials.password
                data = buildJsonObject {
                    put("username", credentials.username)
                    put("clinic_name", credentials.clinicName)
                    put("phone_number", credentials.phoneNumber)
                    put("address", credentials.address)
                    put("display_name", credentials.username)
                }
            }

            if (userSession != null) {
                Result.Success(userSession)
            } else {
                Result.Error("Sign up failed - please try again")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Sign out failed")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.resetPasswordForEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to send password reset email")
        }
    }

    override suspend fun isSessionValid(): Boolean {
        return try {
            val user = getCurrentUser()
            user != null && auth.currentSessionOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
}