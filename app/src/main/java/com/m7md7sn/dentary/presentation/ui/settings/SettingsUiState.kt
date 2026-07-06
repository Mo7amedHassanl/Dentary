package com.m7md7sn.dentary.presentation.ui.settings

sealed class SettingsScreen {
    object Main : SettingsScreen()
    object EditDoctorAndClinicInfo : SettingsScreen()
    object ChangePassword : SettingsScreen()
    object RingtoneCustomization : SettingsScreen()
    object Language : SettingsScreen()
    object DataBackup : SettingsScreen()
    object FAQ : SettingsScreen()
    object ContactSupport : SettingsScreen()
}

data class SettingsUiState(
    val isLoading: Boolean = false,
    val currentScreen: SettingsScreen = SettingsScreen.Main,
    val showLogoutConfirmDialog: Boolean = false,
    
    // Support contact fields
    val supportEmail: String = "",
    val supportMessage: String = "",
    val isSupportEmailError: Boolean = false,
    val isSupportMessageError: Boolean = false,
    val isSupportSending: Boolean = false,

    val passwordChangeSuccess: Boolean = false,
    val passwordChangeError: String? = null,
    val isPasswordChanging: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val newPasswordError: String? = null,
    val confirmPasswordError: String? = null,
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmNewPasswordVisible: Boolean = false,

    // Doctor and clinic info fields
    val fullName: String = "",
    val specialization: String = "",
    val email: String = "",
    val clinicName: String = "",
    val phoneNumber: String = "",
    val clinicAddress: String = "",
    val clinicLogo: String = "",
    val isProfileLoading: Boolean = false,
    val profileError: String? = null,
    val profileUpdateSuccess: Boolean = false,
    val profileUpdateError: String? = null,

    val selectedLanguage: String? = null
)
