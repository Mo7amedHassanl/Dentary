package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.OtpType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

class AuthDataSourceImpl @Inject constructor(
    private val auth: Auth
) : AuthDataSource {

    override suspend fun getCurrentUser(): UserInfo? {
        return try {
            withTimeout(5.seconds) {
                auth.retrieveUserForCurrentSession(updateSession = true)
            }
        } catch (e: Exception) {
            auth.currentUserOrNull()
        }
    }

    override suspend fun login(credentials: LoginCredentials): Result<UserInfo, DataError> {
        return try {
            withTimeout(30.seconds) {
                auth.signInWith(Email) {
                    email = credentials.email
                    password = credentials.password
                }
            }

            val userInfo = auth.currentUserOrNull()
            if (userInfo != null) {
                Result.Success(userInfo)
            } else {
                Result.Error(DataError.Auth.INVALID_CREDENTIALS, "Login failed - user not found")
            }
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Login failed")
        }
    }

    override suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo, DataError> {
        return try {
            val userInfo = auth.signUpWith(Email) {
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

            val sessionUser = userInfo ?: auth.currentUserOrNull()
            if (sessionUser != null) {
                Result.Success(sessionUser)
            } else {
                Result.Error(DataError.Auth.UNKNOWN, "Registration succeeded but no user session found")
            }

        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Registration failed")
        }
    }

    override suspend fun signOut(): Result<Unit, DataError> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Sign out failed")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit, DataError> {
        return try {
            auth.resetPasswordForEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to send reset email")
        }
    }

    override suspend fun isSessionValid(): Boolean {
        return try {
            auth.currentSessionOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun verifyEmailOTP(email: String, token: String): Result<UserInfo, DataError> {
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
                Result.Error(DataError.Auth.UNKNOWN, "Verification succeeded, but user data is not available yet.")
            }
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Verification failed")
        }
    }

    override suspend fun resendEmailVerification(email: String): Result<Unit, DataError> {
        return try {
            auth.resendEmail(
                type = OtpType.Email.SIGNUP,
                email = email
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to resend code")
        }
    }

    override suspend fun verifyPasswordResetOTP(email: String, token: String): Result<Unit, DataError> {
        return try {
            auth.verifyEmailOtp(
                type = OtpType.Email.RECOVERY,
                email = email,
                token = token
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "OTP verification failed")
        }
    }

    override suspend fun resetPasswordWithToken(email: String, token: String, newPassword: String): Result<Unit, DataError> {
        return try {
            val otpResult = verifyPasswordResetOTP(email, token)
            if (otpResult is Result.Error) return otpResult

            auth.updateUser {
                password = newPassword
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to reset password")
        }
    }

    override suspend fun refreshSession(): Result<UserInfo, DataError> {
        return try {
            auth.refreshCurrentSession()
            val userInfo = auth.currentUserOrNull()
            if (userInfo != null) {
                Result.Success(userInfo)
            } else {
                Result.Error(DataError.Auth.SESSION_EXPIRED, "Session expired")
            }
        } catch (e: Exception) {
            Result.Error(e.toDataError(), "Session expired")
        }
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError> {
        return try {
            val user = auth.currentUserOrNull() ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")
            val email = user.email ?: return Result.Error(DataError.Auth.USER_NOT_FOUND, "Email not found")
            
            try {
                // Re-authenticate by signing in again with the current password
                // This refreshes the session's 'amr' (authentication method reference)
                auth.signInWith(Email) {
                    this.email = email
                    this.password = currentPassword
                }
            } catch (e: Exception) {
                return Result.Error(DataError.Auth.INVALID_CREDENTIALS, "Current password is incorrect")
            }
            
            // Modern Supabase might still return "current password required" if configured to demand it in the body.
            // If this fails, the user must disable "Secure password change" in the Supabase Dashboard,
            // as this version of the SDK only supports re-auth via nonce/email for that specific setting.
            auth.updateUser {
                password = newPassword
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message ?: "Failed to change password")
        }
    }
}
