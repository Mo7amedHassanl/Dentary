package com.m7md7sn.dentary.presentation.ui.auth.register

import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo

enum class RegisterStep {
    ACCOUNT_INFO,
    CLINIC_INFO
}

data class RegisterUiState(
    val currentStep: RegisterStep = RegisterStep.ACCOUNT_INFO,
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

    val signupResult: Result<UserInfo, DataError>? = null,

    val needsEmailVerification: Boolean = false,
    val verificationEmail: String? = null
)