package com.m7md7sn.dentary.presentation.ui.auth.login

import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo

data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,

    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    val loginResult: Result<UserInfo>? = null
)
