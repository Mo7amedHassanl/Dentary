package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import com.m7md7sn.dentary.data.source.remote.AuthDataSource
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject
import com.m7md7sn.dentary.utils.Result

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
): AuthRepository {
    override suspend fun getCurrentUser(): UserInfo? {
        return authDataSource.getCurrentUser()
    }

    override suspend fun login(credentials: LoginCredentials): Result<UserInfo> {
        return authDataSource.login(credentials)
    }

    override suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo> {
        return authDataSource.signUp(credentials)
    }

    override suspend fun signOut(): Result<Unit> {
        return authDataSource.signOut()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return authDataSource.sendPasswordResetEmail(email)
    }

    override suspend fun verifyPasswordResetOTP(email: String, token: String): Result<Unit> {
        return authDataSource.verifyPasswordResetOTP(email, token)
    }

    override suspend fun resetPasswordWithToken(email: String, token: String, newPassword: String): Result<Unit> {
        return authDataSource.resetPasswordWithToken(email, token, newPassword)
    }

    override suspend fun isSessionValid(): Boolean {
        return authDataSource.isSessionValid()
    }

    override suspend fun verifyEmailOTP(email: String, token: String): Result<UserInfo> {
        return authDataSource.verifyEmailOTP(email, token)
    }

    override suspend fun resendEmailVerification(email: String): Result<Unit> {
        return authDataSource.resendEmailVerification(email)
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        return authDataSource.changePassword(currentPassword, newPassword)
    }
}