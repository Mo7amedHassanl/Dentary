package com.m7md7sn.dentary.domain.usecase.auth

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credentials: LoginCredentials): Result<UserInfo, DataError> {
        if (credentials.email.isBlank() || credentials.password.isBlank()) {
            return Result.Error(DataError.Auth.INVALID_CREDENTIALS, "Email and password cannot be empty")
        }
        
        // Additional validation can be added here
        
        return authRepository.login(credentials)
    }
}