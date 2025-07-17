package com.m7md7sn.dentary.presentation.ui.auth.register

import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo

data class RegisterUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicPhone: String = "",

    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val clinicNameError: String? = null,
    val clinicAddressError: String? = null,
    val clinicPhoneError: String? = null,

    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,

    val signupResult: Result<UserInfo>? = null,

    val needsEmailVerification: Boolean = false
)
