package com.m7md7sn.dentary.presentation.ui.auth.register

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import com.m7md7sn.dentary.utils.Result
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginHeader
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterHeader
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToEmailVerification: (String) -> Unit = {},
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    LaunchedEffect(uiState.needsEmailVerification) {
        if (uiState.needsEmailVerification) {
            println("RegisterScreen: Navigating to email verification with email: '${uiState.email}'")
            onNavigateToEmailVerification(uiState.email)
            viewModel.resetSignupResult()
        }
    }

    LaunchedEffect(uiState.signupResult) {
        when (uiState.signupResult) {
            is Result.Error -> {
                val errorMessage = (uiState.signupResult as Result.Error).message
                scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                viewModel.resetSignupResult()
            }
            else -> {}
        }
    }


    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
            ) {
                RegisterHeader()
                RegisterContent(
                    onLoginClick = onLoginClick,
                    onRegisterClick = viewModel::register,
                    modifier = Modifier.fillMaxSize(),
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    username = uiState.username,
                    onUsernameValueChange = viewModel::onUsernameChange,
                    password = uiState.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    confirmPassword = uiState.confirmPassword,
                    onConfirmPasswordValueChange = viewModel::onConfirmPasswordChange,
                    isLoading = uiState.isLoading,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError,
                    isPasswordError = uiState.passwordError != null,
                    passwordErrorMessage = uiState.passwordError,
                    isConfirmPasswordError = uiState.confirmPasswordError != null,
                    confirmPasswordErrorMessage = uiState.confirmPasswordError,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                    onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
                    isUsernameError = uiState.usernameError != null,
                    usernameErrorMessage = uiState.usernameError,
                    clinicName = uiState.clinicName,
                    onClinicNameValueChange = viewModel::onClinicNameChange,
                    clinicAddress = uiState.clinicAddress,
                    onClinicAddressValueChange = viewModel::onClinicAddressChange,
                    clinicPhone = uiState.clinicPhone,
                    onClinicPhoneValueChange = viewModel::onClinicPhoneChange,
                    isClinicNameError = uiState.clinicNameError != null,
                    clinicNameErrorMessage = uiState.clinicNameError,
                    isClinicAddressError = uiState.clinicAddressError != null,
                    clinicAddressErrorMessage = uiState.clinicAddressError,
                    isClinicPhoneError = uiState.clinicPhoneError != null,
                    clinicPhoneErrorMessage = uiState.clinicPhoneError,
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

    }
}

@Preview
@Composable
private fun RegisterScreenPreviewEn() {
    DentaryTheme {
        RegisterScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun RegisterScreenPreviewAr() {
    DentaryTheme {
        RegisterScreen()
    }
}