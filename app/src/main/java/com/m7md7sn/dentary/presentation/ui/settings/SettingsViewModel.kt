package com.m7md7sn.dentary.presentation.ui.settings

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.m7md7sn.dentary.R
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
import com.m7md7sn.dentary.utils.LocaleUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val application: Application
) : ViewModel() {

    private var _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<com.m7md7sn.dentary.utils.Event<String>>()
    val snackbarMessage: SharedFlow<com.m7md7sn.dentary.utils.Event<String>> = _snackbarMessage

    private val _eventChannel = Channel<SettingsViewModel.Event>()
    val eventFlow = _eventChannel.receiveAsFlow()

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
        if (_uiState.value.isLoading) return
        
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)

            when (authRepository.signOut()) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    onLogoutSuccess()
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    onLogoutSuccess()
                }
                is Result.Loading -> {
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

    fun toggleCurrentPasswordVisibility() {
        _uiState.value = uiState.value.copy(isCurrentPasswordVisible = !uiState.value.isCurrentPasswordVisible)
    }

    fun toggleNewPasswordVisibility() {
        _uiState.value = uiState.value.copy(isNewPasswordVisible = !uiState.value.isNewPasswordVisible)
    }

    fun toggleConfirmNewPasswordVisibility() {
        _uiState.value = uiState.value.copy(isConfirmNewPasswordVisible = !uiState.value.isConfirmNewPasswordVisible)
    }

    fun validateNewPassword(): Boolean {
        var error: String? = null
        var confirmError: String? = null
        val newPassword = uiState.value.newPassword
        val confirmNewPassword = uiState.value.confirmNewPassword
        
        if (newPassword.length < 8) {
            error = application.getString(R.string.password_min_length)
        } else if (!newPassword.any { it.isDigit() }) {
            error = application.getString(R.string.password_require_number)
        } else if (!newPassword.any { it.isUpperCase() }) {
            error = application.getString(R.string.password_require_uppercase)
        } else if (!newPassword.any { !it.isLetterOrDigit() }) {
            error = application.getString(R.string.password_require_special)
        }
        
        if (newPassword != confirmNewPassword) {
            confirmError = application.getString(R.string.password_mismatch)
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
        if (_uiState.value.isPasswordChanging) return
        
        _uiState.value = uiState.value.copy(isPasswordChanging = true, passwordChangeError = null, passwordChangeSuccess = false)
        viewModelScope.launch {
            when (val result = authRepository.changePassword(currentPassword, newPassword)) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(
                        isPasswordChanging = false,
                        passwordChangeSuccess = true,
                        passwordChangeError = null
                    )
                    _snackbarMessage.emit(Event(application.getString(R.string.password_changed_successfully)))
                }
                is Result.Error -> {
                    val errorMsg = result.message ?: application.getString(R.string.password_change_failed)
                    _uiState.value = uiState.value.copy(
                        isPasswordChanging = false,
                        passwordChangeSuccess = false,
                        passwordChangeError = errorMsg
                    )
                    _snackbarMessage.emit(Event(errorMsg.lines().first()))
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
                        clinicLogo = profile.clinicLogo ?: ""
                    )
                }
                is Result.Error -> {
                    val errorMsg = result.message ?: application.getString(R.string.profile_fetch_failed)
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileError = errorMsg, email = email)
                    _snackbarMessage.emit(Event(errorMsg.lines().first()))
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

    fun saveProfile() {
        if (_uiState.value.isProfileLoading) return
        
        _uiState.value = uiState.value.copy(isProfileLoading = true, profileUpdateError = null, profileUpdateSuccess = false)
        viewModelScope.launch {
            val req = UpdateProfileRequest(
                fullName = uiState.value.fullName,
                clinicName = uiState.value.clinicName,
                phoneNumber = uiState.value.phoneNumber,
                clinicAddress = uiState.value.clinicAddress,
                specialization = uiState.value.specialization,
                clinicLogo = uiState.value.clinicLogo
            )
            when (val result = profileRepository.updateProfile(req)) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileUpdateSuccess = true)
                    _snackbarMessage.emit(Event(application.getString(R.string.profile_updated_successfully)))
                }
                is Result.Error -> {
                    val errorMsg = result.message ?: application.getString(R.string.profile_update_failed)
                    _uiState.value = uiState.value.copy(isProfileLoading = false, profileUpdateError = errorMsg)
                    _snackbarMessage.emit(Event(errorMsg.lines().first()))
                }
                is Result.Loading -> {
                    _uiState.value = uiState.value.copy(isProfileLoading = true)
                }
            }
        }
    }

    fun updateClinicLogo(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProfileLoading = true)
            
            try {
                val currentLogoUrl = _uiState.value.clinicLogo.ifEmpty { null }
                val result = profileRepository.updateClinicLogo(imageUri, currentLogoUrl)
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isProfileLoading = false,
                            clinicLogo = result.data.clinicLogo ?: ""
                        )
                        _snackbarMessage.emit(Event(application.getString(R.string.clinic_logo_updated)))
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(isProfileLoading = false)
                        _snackbarMessage.emit(Event(result.message ?: application.getString(R.string.clinic_logo_update_failed)))
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isProfileLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isProfileLoading = false)
                _snackbarMessage.emit(Event(application.getString(R.string.error_logo_update) + ": ${e.message}"))
            }
        }
    }

    fun onLanguageSelected(language: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedLanguage = language) }
        }
    }

    fun onLanguageSave() {
        viewModelScope.launch {
            val langCode = if (_uiState.value.selectedLanguage == "Arabic") "ar" else "en"
            LocaleUtils.setLocale(application, langCode)
            _eventChannel.send(SettingsViewModel.Event.RecreateActivity)
        }
    }

    init {
        _uiState.value = SettingsUiState(currentScreen = SettingsScreen.Main)

        val savedLanguage = LocaleUtils.getSavedLanguage(application)
        val currentLanguage = if (savedLanguage != null) {
            if (savedLanguage == "ar") "Arabic" else "English"
        } else {
            LocaleUtils.getDisplayLanguage(Locale.getDefault())
        }
        _uiState.update { it.copy(selectedLanguage = currentLanguage) }
    }

    sealed class Event {
        object RecreateActivity : Event()
        data class SendSupportEmail(val email: String, val message: String) : Event()
    }

    // Support functions
    fun onSupportEmailChange(value: String) {
        _uiState.value = uiState.value.copy(supportEmail = value, isSupportEmailError = false)
    }

    fun onSupportMessageChange(value: String) {
        _uiState.value = uiState.value.copy(supportMessage = value, isSupportMessageError = false)
    }

    fun sendSupportInquiry() {
        if (_uiState.value.isSupportSending) return
        // Validate
        val email = uiState.value.supportEmail.trim()
        val message = uiState.value.supportMessage.trim()
        var valid = true
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = uiState.value.copy(isSupportEmailError = true)
            valid = false
        }
        if (message.isBlank()) {
            _uiState.value = uiState.value.copy(isSupportMessageError = true)
            valid = false
        }
        if (!valid) return

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isSupportSending = true)
            // Emit an event to let the UI launch an email intent
            _eventChannel.send(Event.SendSupportEmail(email = email, message = message))
            // Show snackbar that email composer is opening
                _snackbarMessage.emit(Event(application.getString(R.string.opening_email_app)))
            _uiState.value = uiState.value.copy(isSupportSending = false)
            // Optionally clear message
            _uiState.value = uiState.value.copy(supportMessage = "")
        }
    }
}
