package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo
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
            _uiState.value = _uiState.value.copy(isLoading = false, passwordResetResult = result)

            if (result is Result.Success) {
                _snackbarMessage.emit(Event("Password reset email sent!"))
            } else if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message))
            }
        }
    }

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

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun resetPasswordResetResult() {
        _uiState.value = _uiState.value.copy(passwordResetResult = null)
    }
}