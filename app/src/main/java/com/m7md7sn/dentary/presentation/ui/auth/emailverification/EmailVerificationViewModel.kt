package com.m7md7sn.dentary.presentation.ui.auth.emailverification

import com.m7md7sn.dentary.presentation.ui.auth.emailverification.EmailVerificationUiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Event
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email: String = savedStateHandle.get<String>("email") ?: ""

    private val _uiState = MutableStateFlow(EmailVerificationUiState(email = email))
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun verifyOTP() {
        val currentState = _uiState.value

        val validationResult = validateOTPCode(currentState.otpCode)

        if (validationResult != null) {
            _uiState.value = currentState.copy(otpError = validationResult)
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            verificationResult = Result.Loading,
            otpError = null
        )

        viewModelScope.launch {

            val emailToVerify = currentState.email.takeIf { it.isNotBlank() } ?: email

            if (emailToVerify.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    otpError = "Email address is missing. Please try registering again."
                )
                return@launch
            }

            val result = authRepository.verifyEmailOTP(emailToVerify, currentState.otpCode)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                verificationResult = result
            )

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.emit(Event("Email verified successfully!"))
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> { /* Already handled above */ }
            }
        }
    }

    fun resendVerificationCode() {
        if (!_uiState.value.canResend) return

        _uiState.value = _uiState.value.copy(isResending = true)

        viewModelScope.launch {
            val emailToResend = _uiState.value.email.takeIf { it.isNotBlank() } ?: email

            if (emailToResend.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    isResending = false,
                    otpError = "Unable to get email for resend."
                )
                _snackbarMessage.emit(Event("Unable to get email for resend"))
                return@launch
            }

            val result = authRepository.resendEmailVerification(emailToResend)
            _uiState.value = _uiState.value.copy(
                isResending = false,
                resendResult = result
            )

            when (result) {
                is Result.Success -> {
                    _snackbarMessage.emit(Event("Verification code sent!"))
                    startResendCountdown()
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> { /* Not applicable for this case */ }
            }
        }
    }

    fun onOTPCodeChange(newValue: String) {
        val filteredValue = newValue.filter { it.isDigit() }.take(6)
        _uiState.value = _uiState.value.copy(otpCode = filteredValue, otpError = null)

        if (filteredValue.length == 6) {
            verifyOTP()
        }
    }

    private fun validateOTPCode(code: String): String? {
        return when {
            code.isBlank() -> "Please enter verification code"
            code.length != 6 -> "Verification code must be 6 digits"
            !code.all { it.isDigit() } -> "Verification code must contain only numbers"
            else -> null
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

    fun resetVerificationResult() {
        _uiState.value = _uiState.value.copy(verificationResult = null)
    }
}
