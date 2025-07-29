package com.m7md7sn.dentary.presentation.ui.settings

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.settings.components.DoctorAndClinicInfoSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.LogoutConfirmationDialog
import com.m7md7sn.dentary.presentation.ui.settings.components.PasswordChangeSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.RingtoneCustomizationSettings
import com.m7md7sn.dentary.presentation.ui.settings.components.SettingsContent

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

    // Show logout confirmation dialog
    if (uiState.showLogoutConfirmDialog) {
        LogoutConfirmationDialog(
            onConfirm = { viewModel.confirmLogout(onNavigateToLogin) },
            onDismiss = { viewModel.hideLogoutConfirmDialog() }
        )
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
                            focusManager = focusManager
                        )
                    }
                    is SettingsScreen.ChangePassword -> {
                        PasswordChangeSettings(
                            focusManager = focusManager
                        )
                    }
                    is SettingsScreen.RingtoneCustomization -> {
                        RingtoneCustomizationSettings()
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