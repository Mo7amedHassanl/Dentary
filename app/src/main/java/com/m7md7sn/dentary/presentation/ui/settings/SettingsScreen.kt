package com.m7md7sn.dentary.presentation.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.settings.components.DoctorAndClinicInfoSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.LogoutConfirmationDialog
import com.m7md7sn.dentary.presentation.ui.settings.components.PasswordChangeSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.AppLanguageSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.RingtoneCustomizationSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.SettingsContent
import com.m7md7sn.dentary.presentation.ui.settings.components.SupportContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Show logout confirmation dialog
    if (uiState.showLogoutConfirmDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.confirmLogout(onNavigateToLogin) },
            onDismiss = { viewModel.hideLogoutConfirmDialog() }
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SettingsViewModel.Event.RecreateActivity -> {
                    (context as? Activity)?.recreate()
                }
            }
        }
    }

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true)
                    .padding(vertical = 8.dp, horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (uiState.currentScreen) {
                    is SettingsScreen.Main -> {
                        SettingsContent(
                            onNavigateToScreen = { screen ->
                                viewModel.navigateToScreen(screen)
                            },
                            onLogout = {
                                viewModel.showLogoutConfirmDialog()
                            }
                        )
                    }

                    is SettingsScreen.EditDoctorAndClinicInfo -> {
                        DoctorAndClinicInfoSettings(
                            focusManager = focusManager,
                            uiState = uiState,
                            onFullNameChange = viewModel::onFullNameChange,
                            onSpecializationChange = viewModel::onSpecializationChange,
                            onClinicNameChange = viewModel::onClinicNameChange,
                            onPhoneNumberChange = viewModel::onPhoneNumberChange,
                            onClinicAddressChange = viewModel::onClinicAddressChange,
                            onClinicLogoChange = viewModel::onClinicLogoChange,
                            onSaveClick = viewModel::saveProfile,
                            onBackClick = { viewModel.navigateBack() },
                            fetchProfile = viewModel::fetchProfile,
                            snackbarHostState = snackbarHostState,
                            snackbarMessageFlow = viewModel.snackbarMessage
                        )
                    }

                    is SettingsScreen.ChangePassword -> {
                        PasswordChangeSettings(
                            focusManager = focusManager,
                            uiState = uiState,
                            onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
                            onNewPasswordChange = viewModel::onNewPasswordChange,
                            onConfirmNewPasswordChange = viewModel::onConfirmNewPasswordChange,
                            validateNewPassword = viewModel::validateNewPassword,
                            changePassword = viewModel::changePassword,
                            clearPasswordFields = viewModel::clearPasswordFields,
                            clearPasswordChangeState = viewModel::clearPasswordChangeState,
                            onBackClick = { viewModel.navigateBack() },
                            snackbarHostState = snackbarHostState,
                            snackbarMessageFlow = viewModel.snackbarMessage
                        )
                    }

                    is SettingsScreen.RingtoneCustomization -> {
                        RingtoneCustomizationSettings()
                    }

                    is SettingsScreen.Language -> {
                        uiState.selectedLanguage?.let {
                            AppLanguageSettings(
                                selectedLanguage = it,
                                onLanguageSelected = { lang -> viewModel.onLanguageSelected(lang) },
                                onBackClick = { viewModel.navigateBack() },
                                onSaveClicked = { viewModel.onLanguageSave() }
                            )
                        }
                    }

                    is SettingsScreen.ContactSupport -> {
                        SupportContent(
                            email = "",
                            message = "",
                            isEmailError = false,
                            isMessageError = false,
                            isLoading = false,
                            onEmailChange = {  },
                            onMessageChange = {  },
                            onSendClick = {  },
                            focusManager = focusManager
                        )
                    }
                    // For screens that are not implemented yet, just show main content
                    else -> {
                        SettingsContent(
                            onNavigateToScreen = { screen ->
                                viewModel.navigateToScreen(screen)
                            },
                            onLogout = {
                                viewModel.showLogoutConfirmDialog()
                            }
                        )
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

    }
}

@Preview
@Composable
private fun SettingsScreenPreviewEn() {
    DentaryTheme {
        SettingsScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun SettingsScreenPreviewAr() {
    DentaryTheme {
        SettingsScreen()
    }
}