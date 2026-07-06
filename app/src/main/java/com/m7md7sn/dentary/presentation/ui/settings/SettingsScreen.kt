package com.m7md7sn.dentary.presentation.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
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
    
    // Show password change success dialog
    if (uiState.passwordChangeSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.clearPasswordChangeState() },
            title = {
                Text(
                    text = stringResource(R.string.success),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                Text(
                    text = "Password changed successfully",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearPasswordChangeState() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DentaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = AlexandriaRegular,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            containerColor = BackgroundColor,
            titleContentColor = DentaryDarkBlue,
            textContentColor = DentaryBlue,
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SettingsViewModel.Event.RecreateActivity -> {
                    (context as? Activity)?.recreate()
                }
                is SettingsViewModel.Event.SendSupportEmail -> {
                    // Launch email composer
                    try {
                        val uriText = "mailto:dev.m7md7asn@gmail.com?subject=" +
                                android.net.Uri.encode("Dentary Support") +
                                "&body=" + android.net.Uri.encode("From: ${event.email}\n\n${event.message}")
                        val intent = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
                            data = android.net.Uri.parse(uriText)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // fallback: try generic SEND intent
                        try {
                            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "message/rfc822"
                                putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("dev.m7md7asn@gmail.com"))
                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Dentary Support")
                                putExtra(android.content.Intent.EXTRA_TEXT, "From: ${event.email}\n\n${event.message}")
                            }
                            context.startActivity(android.content.Intent.createChooser(intent, "Send email"))
                        } catch (ex: Exception) {
                            // show snackbar if cannot launch
                            snackbarHostState.showSnackbar("Unable to open email app")
                        }
                    }
                }
            }
        }
    }

    // Show snackbar messages emitted by ViewModel
    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { evt ->
            evt.getContentIfNotHandled()?.let { msg ->
                snackbarHostState.showSnackbar(msg)
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
                            onClinicLogoUpdate = viewModel::updateClinicLogo,
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
                            toggleCurrentPasswordVisibility = viewModel::toggleCurrentPasswordVisibility,
                            toggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
                            toggleConfirmNewPasswordVisibility = viewModel::toggleConfirmNewPasswordVisibility,
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
                                email = uiState.supportEmail,
                                message = uiState.supportMessage,
                                isEmailError = uiState.isSupportEmailError,
                                isMessageError = uiState.isSupportMessageError,
                                isLoading = uiState.isSupportSending,
                                onEmailChange = viewModel::onSupportEmailChange,
                                onMessageChange = viewModel::onSupportMessageChange,
                                onSendClick = viewModel::sendSupportInquiry,
                                onBackClick = { viewModel.navigateBack() },
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

            if (uiState.isProfileLoading || uiState.isPasswordChanging || uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
