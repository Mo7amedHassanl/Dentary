package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.model.SignUpCredentials
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.buildJsonObject
import org.slf4j.MDC.put
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val auth: Auth
) : AuthDataSource {
    override suspend fun login(credentials: LoginCredentials): Result<UserInfo> {
        return try {
            auth.signUpWith(Email) {
                email = credentials.email
                password = credentials.password
            }
            val userInfo = auth.currentUserOrNull()
            if (userInfo != null) {
                Result.success(userInfo)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(credentials: SignUpCredentials): Result<UserInfo> {
        return try {
            val user = auth.signUpWith(Email) {
                email = credentials.email
                password = credentials.password
                data = buildJsonObject {
                    put("username", credentials.username)
                    put("clinic_name", credentials.clinicName)
                    put("phone_number", credentials.phoneNumber)
                    put("address", credentials.address)
                }
            }
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}