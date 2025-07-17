package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import com.m7md7sn.dentary.utils.Result

enum class PasswordResetStep {
    EMAIL_INPUT,
    OTP_VERIFICATION,
    PASSWORD_CHANGE
}

data class PasswordResetUiState(
    val currentStep: PasswordResetStep = PasswordResetStep.EMAIL_INPUT,
    val email: String = "",
    val emailError: String? = null,
    val otpCode: String = "",
    val otpError: String? = null,
    val newPassword: String = "",
    val confirmPassword: String = "",
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isResending: Boolean = false,
    val passwordResetResult: Result<Unit>? = null,
    val otpVerificationResult: Result<Unit>? = null,
    val passwordChangeResult: Result<Unit>? = null,
    val canResend: Boolean = true,
    val resendCountdown: Int = 0
)
