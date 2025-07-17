package com.m7md7sn.dentary.presentation.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.SignUpCredentials
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Event
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterValidationResult(
    val isValid: Boolean,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val clinicNameError: String? = null,
    val clinicAddressError: String? = null,
    val clinicPhoneError: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun register() {
        val currentState = _uiState.value
        val validationResult = validateAllInputs(currentState)

        if (!validationResult.isValid) {
            _uiState.value = currentState.copy(
                usernameError = validationResult.usernameError,
                emailError = validationResult.emailError,
                passwordError = validationResult.passwordError,
                confirmPasswordError = validationResult.confirmPasswordError,
                clinicNameError = validationResult.clinicNameError,
                clinicAddressError = validationResult.clinicAddressError,
                clinicPhoneError = validationResult.clinicPhoneError
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, signupResult = Result.Loading)
        viewModelScope.launch {
            val result = authRepository.signUp(
                SignUpCredentials(
                    email = _uiState.value.email,
                    username = _uiState.value.username,
                    password = _uiState.value.password,
                    clinicName = _uiState.value.clinicName,
                    address = _uiState.value.clinicAddress,
                    phoneNumber = _uiState.value.clinicPhone
                )
            )

            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        signupResult = result,
                        needsEmailVerification = true
                    )
                    _snackbarMessage.emit(Event("Registration successful! Please check your email for verification code."))
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        signupResult = result,
                        needsEmailVerification = false
                    )
                    _snackbarMessage.emit(Event(result.message))
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, emailError = null)
    }

    fun onUsernameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(username = newValue, usernameError = null)
    }

    fun onPasswordChange(newValue: String) {
        _uiState.value = _uiState.value.copy(password = newValue, passwordError = null)
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.value =
            _uiState.value.copy(confirmPassword = newValue, confirmPasswordError = null)
    }

    fun onClinicNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(clinicName = newValue, clinicNameError = null)
    }

    fun onClinicAddressChange(newValue: String) {
        _uiState.value = _uiState.value.copy(clinicAddress = newValue, clinicAddressError = null)
    }

    fun onClinicPhoneChange(newValue: String) {
        _uiState.value = _uiState.value.copy(clinicPhone = newValue, clinicPhoneError = null)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value =
            _uiState.value.copy(isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible)
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username cannot be empty"
            username.length < 3 -> "Username must be at least 3 characters"
            username.length > 20 -> "Username cannot exceed 20 characters"
            !username.matches(Regex("^[a-zA-Z0-9._]+$")) -> "Username can only contain letters, numbers, dots, and underscores"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            !password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) } -> "Password must contain at least one special character"
            else -> null
        }
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isBlank() -> "Confirm Password cannot be empty"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }

    private fun validateClinicName(clinicName: String): String? {
        return when {
            clinicName.isBlank() -> "Clinic Name cannot be empty"
            clinicName.length < 2 -> "Clinic Name must be at least 2 characters"
            clinicName.length > 100 -> "Clinic Name cannot exceed 100 characters"
            else -> null
        }
    }

    private fun validateClinicAddress(address: String): String? {
        return when {
            address.isBlank() -> "Clinic Address cannot be empty"
            address.length < 10 -> "Please provide a complete address"
            address.length > 200 -> "Address cannot exceed 200 characters"
            else -> null
        }
    }

    private fun validateClinicPhone(phone: String): String? {
        val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
        return when {
            phone.isBlank() -> "Clinic Phone cannot be empty"
            cleanPhone.length < 10 -> "Phone number must be at least 10 digits"
            cleanPhone.length > 15 -> "Phone number cannot exceed 15 digits"
            !phone.matches(Regex("^[+]?[0-9\\s\\-()]+$")) -> "Invalid phone number format"
            else -> null
        }
    }

    private fun validateAllInputs(state: RegisterUiState): RegisterValidationResult {
        val usernameError = validateUsername(state.username)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val confirmPasswordError = validateConfirmPassword(state.password, state.confirmPassword)
        val clinicNameError = validateClinicName(state.clinicName)
        val clinicAddressError = validateClinicAddress(state.clinicAddress)
        val clinicPhoneError = validateClinicPhone(state.clinicPhone)

        return RegisterValidationResult(
            isValid = listOf(
                usernameError, emailError, passwordError, confirmPasswordError,
                clinicNameError, clinicAddressError, clinicPhoneError
            ).all { it == null },
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            clinicNameError = clinicNameError,
            clinicAddressError = clinicAddressError,
            clinicPhoneError = clinicPhoneError
        )
    }

    fun resetSignupResult() {
        _uiState.value = _uiState.value.copy(signupResult = null)
    }
}