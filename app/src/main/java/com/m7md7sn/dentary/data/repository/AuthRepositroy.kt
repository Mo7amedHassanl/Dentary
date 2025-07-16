package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Result<UserInfo>
    suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo>
    suspend fun signOut(): Result<Unit>
}