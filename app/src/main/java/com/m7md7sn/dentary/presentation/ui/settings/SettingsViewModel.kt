package com.m7md7sn.dentary.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import com.m7md7sn.dentary.utils.Event
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m7md7sn.dentary.data.repository.ProfileRepository
import com.m7md7sn.dentary.data.model.UpdateProfileRequest

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage

    fun navigateToScreen(screen: SettingsScreen) {
        _uiState.value = uiState.value.copy(currentScreen = screen)
    }

    fun navigateBack() {
        _uiState.value = uiState.value.copy(currentScreen = SettingsScreen.Main)
    }

    fun showLogoutConfirmDialog() {
        _uiState.value = uiState.value.copy(showLogoutConfirmDialog = true)
    }

    fun hideLogoutConfirmDialog() {
        _uiState.value = uiState.value.copy(showLogoutConfirmDialog = false)
    }

    fun confirmLogout(onLogoutSuccess: () -> Unit) {
        hideLogoutConfirmDialog()
        logout(onLogoutSuccess)
    }

    private fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)

            when (val result = authRepository.signOut()) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    onLogoutSuccess()
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    // Handle error if needed - for now we'll still call onLogoutSuccess
                    // since local logout should always work
                    onLogoutSuccess()
                }
                is Result.Loading -> {
                    // Loading state is already set above
                }
            }
        }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.value = uiState.value.copy(currentPassword = value)
    }
    fun onNewPasswordChange(value: String) {
        _uiState.value = uiState.value.copy(newPassword = value, newPasswordError = null)
    }
    fun onConfirmNewPasswordChange(value: String) {
        _uiState.value = uiState.value.copy(confirmNewPassword = value, confirmPasswordError = null)
    }
    fun validateNewPassword(): Boolean {
        var error: String? = null
        var confirmError: String? = null
        val newPassword = uiState.value.newPassword
        val confirmNewPassword = uiState.value.confirmNewPassword
        if (newPassword.length < 8) {
            error = "Password must be at least 8 characters."
        } else if (!newPassword.any { it.isDigit() }) {
            error = "Password must contain at least one number."
        }
        if (newPassword != confirmNewPassword) {
            confirmError = "Passwords do not match."
        }
        _uiState.value = uiState.value.copy(newPasswordError = error, confirmPasswordError = confirmError)
        return error == null && confirmError == null
    }
    fun clearPasswordFields() {
        _uiState.value = uiState.value.copy(
            currentPassword = "",
            newPassword = "",
            confirmNewPassword = "",
            newPasswordError = null,
            confirmPasswordError = null
        )
    }

    fun changePassword(currentPassword: String = uiState.value.currentPassword, newPassword: String = uiState.value.newPassword) {
        _uiState.value = uiState.value.copy(isPasswordChanging = true, passwordChangeError = null, passwordChangeSuccess = false)
        viewModelScope.launch {
            when (val result = authRepository.changePassword(currentPassword, newPassword)) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(
                        isPasswordChanging = false,
                        passwordChangeSuccess = true,
                        passwordChangeError = null
                    )
                    _snackbarMessage.emit(Event("Password changed successfully"))
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(
                        isPasswordChanging = false,
                        passwordChangeSuccess = false,
                        passwordChangeError = result.message
                    )
                    _snackbarMessage.emit(Event(result.message.lines().first()))
                }
                is Result.Loading -> {
                    _uiState.value = uiState.value.copy(isPasswordChanging = true)
                }
            }
        }
    }

    fun clearPasswordChangeState() {
        _uiState.value = uiState.value.copy(passwordChangeSuccess = false, passwordChangeError = null)
    }

    fun fetchProfile() {
        _uiState.value = uiState.value.copy(isProfileLoading = true, profileError = null)
        viewModelScope.launch {
            val email = authRepository.getCurrentUser()?.email ?: ""
            when (val result = profileRepository.getProfile()) {
                is Result.Success -> {
                    val profile = result.data
                    _uiState.value = uiState.value.copy(
                        isProfileLoading = false,
                        fullName = profile.fullName ?: "",
                        clinicName = profile.clinicName ?: "",
                        phoneNumber = profile.phoneNumber ?: "",
                        clinicAddress = profile.clinicAddress ?: "",
                        email = email,
                        specialization = profile.specialization ?: "",
                        clinicLogo = ""
                    )
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileError = result.message, email = email)
                    _snackbarMessage.emit(Event(result.message.lines().first()))
                }
                is Result.Loading -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = true, email = email)
                }
            }
        }
    }

    fun onFullNameChange(value: String) {
        _uiState.value = uiState.value.copy(fullName = value)
    }
    fun onSpecializationChange(value: String) {
        _uiState.value = uiState.value.copy(specialization = value)
    }
    fun onEmailChange(value: String) {
        _uiState.value = uiState.value.copy(email = value)
    }
    fun onClinicNameChange(value: String) {
        _uiState.value = uiState.value.copy(clinicName = value)
    }
    fun onPhoneNumberChange(value: String) {
        _uiState.value = uiState.value.copy(phoneNumber = value)
    }
    fun onClinicAddressChange(value: String) {
        _uiState.value = uiState.value.copy(clinicAddress = value)
    }
    fun onClinicLogoChange(value: String) {
        _uiState.value = uiState.value.copy(clinicLogo = value)
    }

    fun saveProfile() {
        _uiState.value = uiState.value.copy(isProfileLoading = true, profileUpdateError = null, profileUpdateSuccess = false)
        viewModelScope.launch {
            val req = UpdateProfileRequest(
                fullName = uiState.value.fullName,
                clinicName = uiState.value.clinicName,
                phoneNumber = uiState.value.phoneNumber,
                clinicAddress = uiState.value.clinicAddress,
                specialization = uiState.value.specialization
            )
            when (val result = profileRepository.updateProfile(req)) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileUpdateSuccess = true)
                    _snackbarMessage.emit(Event("Profile updated successfully"))
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileUpdateError = result.message)
                    _snackbarMessage.emit(Event(result.message.lines().first()))
                }
                is Result.Loading -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = true)
                }
            }
        }
    }

    init {
        _uiState.value = SettingsUiState(currentScreen = SettingsScreen.Main)
    }
}