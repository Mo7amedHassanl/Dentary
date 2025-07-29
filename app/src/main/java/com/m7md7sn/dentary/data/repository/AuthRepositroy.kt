package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result
interface AuthRepository {
    suspend fun getCurrentUser(): UserInfo?
    suspend fun login(credentials: LoginCredentials): Result<UserInfo>
    suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo>
    suspend fun signOut(): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun verifyPasswordResetOTP(email: String, token: String): Result<Unit>
    suspend fun resetPasswordWithToken(email: String, token: String, newPassword: String): Result<Unit>
    suspend fun isSessionValid(): Boolean
    suspend fun verifyEmailOTP(email: String, token: String): Result<UserInfo>
    suspend fun resendEmailVerification(email: String): Result<Unit>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
}