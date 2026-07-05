package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

enum class PasswordResetStep {
    EMAIL_INPUT,
    OTP_VERIFICATION,
    PASSWORD_CHANGE,
    SUCCESS
}

data class PasswordResetUiState(
    val currentStep: PasswordResetStep = PasswordResetStep.EMAIL_INPUT,
    val email: String = "",
    val emailError: String? = null,
    val otpCode: String = "",
    val otpError: String? = null,
    val newPassword: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isResending: Boolean = false,
    val canResend: Boolean = true,
    val resendCountdown: Int = 0,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val passwordResetResult: Result<Unit, DataError>? = null,
    val otpVerificationResult: Result<Unit, DataError>? = null,
    val passwordChangeResult: Result<Unit, DataError>? = null,
    val isSuccess: Boolean = false
)