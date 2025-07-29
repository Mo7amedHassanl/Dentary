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
)