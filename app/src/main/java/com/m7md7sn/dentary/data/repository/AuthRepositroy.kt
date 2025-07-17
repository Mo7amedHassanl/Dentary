package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.user.UserInfo
import com.m7md7sn.dentary.utils.Result
interface AuthRepository {
    suspend fun getCurrentUser(): UserInfo?
    suspend fun login(credentials: LoginCredentials): com.m7md7sn.dentary.utils.Result<UserInfo>
    suspend fun signUp(credentials: SignUpCredentials): com.m7md7sn.dentary.utils.Result<UserInfo>
    suspend fun signOut(): com.m7md7sn.dentary.utils.Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): com.m7md7sn.dentary.utils.Result<Unit>
    suspend fun isSessionValid(): Boolean
}