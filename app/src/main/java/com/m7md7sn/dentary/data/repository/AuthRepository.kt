package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import com.m7md7sn.dentary.domain.model.DataError
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result

interface AuthRepository {
    suspend fun getCurrentUser(): UserInfo?
    suspend fun login(credentials: LoginCredentials): Result<UserInfo, DataError>
    suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo, DataError>
    suspend fun signOut(): Result<Unit, DataError>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit, DataError>
    suspend fun verifyPasswordResetOTP(email: String, token: String): Result<Unit, DataError>
    suspend fun resetPasswordWithToken(email: String, token: String, newPassword: String): Result<Unit, DataError>
    suspend fun isSessionValid(): Boolean
    suspend fun verifyEmailOTP(email: String, token: String): Result<UserInfo, DataError>
    suspend fun resendEmailVerification(email: String): Result<Unit, DataError>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError>
}