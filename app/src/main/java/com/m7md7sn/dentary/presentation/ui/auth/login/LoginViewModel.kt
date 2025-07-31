package com.m7md7sn.dentary.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Event
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ValidationResult(
    val isValid: Boolean,
    val emailError: String? = null,
    val passwordError: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun login() {
        val currentUiState = _uiState.value
        val validationResult = validateLoginInputs(currentUiState.email, currentUiState.password)

        if (!validationResult.isValid) {
            _uiState.value = currentUiState.copy(
                emailError = validationResult.emailError,
                passwordError = validationResult.passwordError
            )
            return
        }

        _uiState.value = currentUiState.copy(isLoading = true, loginResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.login(
                LoginCredentials(
                    email = currentUiState.email,
                    password = currentUiState.password
                )
            )
            _uiState.value = _uiState.value.copy(isLoading = false, loginResult = result)

            if (result is Result.Error) {
                _snackbarMessage.emit(Event(result.message.lines().first()))
            }
        }
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue, passwordError = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun resetLoginResult() {
        _uiState.value = _uiState.value.copy(loginResult = null)
    }

    private fun validateLoginInputs(email: String, password: String): ValidationResult {
        val emailError = when {
            email.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Password cannot be empty"
            else -> null
        }

        return ValidationResult(
            isValid = emailError == null && passwordError == null,
            emailError = emailError,
            passwordError = passwordError
        )
    }
}