package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

data class PasswordResetValidationResult(
    val isValid: Boolean,
    val emailError: String? = null
)

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordResetUiState())
    val uiState: StateFlow<PasswordResetUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun sendPasswordResetEmail() {
        val currentUiState = _uiState.value
        val validationResult = validatePasswordResetInputs(currentUiState.email)

        if (!validationResult.isValid) {
            _uiState.value = currentUiState.copy(
                emailError = validationResult.emailError
            )
            return
        }

        _uiState.value = currentUiState.copy(isLoading = true, passwordResetResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(currentUiState.email)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                passwordResetResult = result
            )

            if (result is Result.Success) {
                _snackbarMessage.emit(Event("Password reset OTP sent!"))
                _uiState.value = _uiState.value.copy(
                    currentStep = PasswordResetStep.OTP_VERIFICATION
                )
                startResendCountdown()
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

    fun verifyOTP() {
        val currentState = _uiState.value
        val validationResult = validateOTPCode(currentState.otpCode)

        if (validationResult != null) {
            _uiState.value = currentState.copy(otpError = validationResult)
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            otpVerificationResult = Result.Loading,
            otpError = null
        )

        viewModelScope.launch {
            val result = authRepository.verifyPasswordResetOTP(currentState.email, currentState.otpCode)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                otpVerificationResult = result
            )

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.emit(Event("OTP verified successfully!"))
                    _uiState.value = _uiState.value.copy(
                        currentStep = PasswordResetStep.PASSWORD_CHANGE
                    )
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> { /* Already handled above */ }
            }
        }
    }

    fun resetPassword() {
        val currentState = _uiState.value
        val passwordValidation = validatePasswords(currentState.newPassword, currentState.confirmPassword)

        if (!passwordValidation.isValid) {
            _uiState.value = currentState.copy(
                passwordError = passwordValidation.passwordError,
                confirmPasswordError = passwordValidation.confirmPasswordError
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            passwordChangeResult = Result.Loading,
            passwordError = null,
            confirmPasswordError = null
        )

        viewModelScope.launch {
            val result = authRepository.resetPasswordWithToken(
                currentState.email,
                currentState.otpCode,
                currentState.newPassword
            )
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                passwordChangeResult = result
            )

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.emit(Event("Password reset successfully!"))
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> { /* Already handled above */ }
            }
        }
    }

    fun resendPasswordResetEmail() {
        if (!_uiState.value.canResend) return

        _uiState.value = _uiState.value.copy(isResending = true)

        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(_uiState.value.email)
            _uiState.value = _uiState.value.copy(
                isResending = false,
                passwordResetResult = result
            )

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.emit(Event("Password reset OTP resent!"))
                    startResendCountdown()
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> { /* Not applicable for this case */ }
            }
        }
    }

    private fun startResendCountdown() {
        _uiState.value = _uiState.value.copy(canResend = false, resendCountdown = 180)

        viewModelScope.launch {
            repeat(180) {
                delay(1000)
                val currentCountdown = _uiState.value.resendCountdown - 1
                _uiState.value = _uiState.value.copy(resendCountdown = currentCountdown)

                if (currentCountdown <= 0) {
                    _uiState.value = _uiState.value.copy(canResend = true, resendCountdown = 0)
                }
            }
        }
    }

    // Input change handlers
    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun onOTPCodeChange(newValue: String) {
        val filteredValue = newValue.filter { it.isDigit() }.take(6)
        _uiState.value = _uiState.value.copy(otpCode = filteredValue, otpError = null)

        if (filteredValue.length == 6) {
            verifyOTP()
        }
    }

    fun onNewPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(newPassword = newValue, passwordError = null)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newValue, confirmPasswordError = null)
    }

    // Validation functions
    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            email.length > 254 -> "Email address is too long"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email address"
            !isValidEmailDomain(email) -> "Please enter an email with a valid domain"
            else -> null
        }
    }

    private fun isValidEmailDomain(email: String): Boolean {
        val domain = email.substringAfter("@", "")
        return when {
            domain.isEmpty() -> false
            domain.length < 3 -> false
            !domain.contains(".") -> false
            domain.startsWith(".") || domain.endsWith(".") -> false
            domain.contains("..") -> false
            else -> true
        }
    }

    private fun validatePasswordResetInputs(email: String): PasswordResetValidationResult {
        val emailError = validateEmail(email)

        return PasswordResetValidationResult(
            isValid = emailError == null,
            emailError = emailError
        )
    }

    private fun validateOTPCode(code: String): String? {
        return when {
            code.isBlank() -> "Please enter verification code"
            code.length != 6 -> "Verification code must be 6 digits"
            !code.all { it.isDigit() } -> "Verification code must contain only numbers"
            else -> null
        }
    }

    private fun validatePasswords(password: String, confirmPassword: String): PasswordValidationResult {
        val passwordError = validatePassword(password)
        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }

        return PasswordValidationResult(
            isValid = passwordError == null && confirmPasswordError == null,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            else -> null
        }
    }

    // Reset functions
    fun resetPasswordResetResult() {
        _uiState.value = _uiState.value.copy(passwordResetResult = null)
    }

    fun resetOtpVerificationResult() {
        _uiState.value = _uiState.value.copy(otpVerificationResult = null)
    }

    fun resetPasswordChangeResult() {
        _uiState.value = _uiState.value.copy(passwordChangeResult = null)
    }
}

data class PasswordValidationResult(
    val isValid: Boolean,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)
