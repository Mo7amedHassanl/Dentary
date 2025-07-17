package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.OtpType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException

class AuthDataSourceImpl @Inject constructor(
    private val auth: Auth
) : AuthDataSource {

    override suspend fun getCurrentUser(): UserInfo? {
        return try {
            // Add timeout to prevent hanging requests
            withTimeout(10000) { // 10 second timeout
                auth.retrieveUserForCurrentSession(updateSession = true)
            }
        } catch (timeoutException: TimeoutCancellationException) {
            null
        } catch (e: Exception) {
            try {
                withTimeout(5000) { // 5 second timeout for refresh
                    auth.refreshCurrentSession()
                    auth.currentUserOrNull()
                }
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

    override suspend fun verifyEmailOTP(email: String, token: String): Result<UserInfo> {
        return try {
            auth.verifyEmailOtp(
                type = OtpType.Email.SIGNUP,
                email = email,
                token = token
            )
            val userInfo = auth.currentUserOrNull()
            if (userInfo != null) {
                Result.Success(userInfo)
            } else {
                Result.Error("Email verification failed")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Email verification failed")
        }
    }

    override suspend fun resendEmailVerification(email: String): Result<Unit> {
        return try {
            // This should resend signup verification, not password reset
            auth.resendEmail(
                type = OtpType.Email.SIGNUP,
                email = email
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to resend verification email")
        }
    }

    override suspend fun verifyPasswordResetOTP(email: String, token: String): Result<Unit> {
        return try {
            // Sign out any existing session to ensure clean state
            try {
                auth.signOut()
            } catch (e: Exception) {
                // Ignore if already signed out
            }

            auth.verifyEmailOtp(
                type = io.github.jan.supabase.auth.OtpType.Email.RECOVERY,
                email = email,
                token = token
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to verify password reset OTP")
        }
    }

    override suspend fun resetPasswordWithToken(email: String, token: String, newPassword: String): Result<Unit> {
        return try {
            auth.updateUser {
                password = newPassword
            }

            // Sign out the user after password reset to ensure they need to log in again
            auth.signOut()

            Result.Success(Unit)
        } catch (e: Exception) {
            println("Error resetting password: ${e.message}")
            Result.Error(e.message ?: "Failed to reset password")
        }
    }

    override suspend fun refreshSession(): Result<UserInfo> {
        return try {
            withTimeout(10000) {
                auth.refreshCurrentSession()
                val userInfo = auth.currentUserOrNull()
                if (userInfo != null) {
                    Result.Success(userInfo)
                } else {
                    Result.Error("Session refresh failed - please log in again")
                }
            }
        } catch (timeoutException: TimeoutCancellationException) {
            Result.Error("Session refresh timed out")
        } catch (e: Exception) {
            Result.Error("Session expired - please log in again")
        }
    }
}