package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import androidx.compose.foundation.layout.Box
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
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.components.EmailVerificationContent
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.components.PasswordResetContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterHeader
import com.m7md7sn.dentary.utils.Event
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(
    onPasswordResetSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasswordResetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event: Event<String> ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                    if (message == "Password reset successfully!") {
                        onPasswordResetSuccess()
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.passwordResetResult) {
        if (uiState.passwordResetResult != null) {
            viewModel.resetPasswordResetResult()
        }
    }

    LaunchedEffect(uiState.otpVerificationResult) {
        if (uiState.otpVerificationResult != null) {
            viewModel.resetOtpVerificationResult()
        }
    }

    LaunchedEffect(uiState.passwordChangeResult) {
        if (uiState.passwordChangeResult != null) {
            viewModel.resetPasswordChangeResult()
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
                    .verticalScroll(scrollState),
            ) {
                RegisterHeader()
                PasswordResetContent(
                    currentStep = uiState.currentStep,
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    onSendPasswordResetClick = viewModel::sendPasswordResetEmail,
                    onResendPasswordResetClick = viewModel::resendPasswordResetEmail,
                    otpCode = uiState.otpCode,
                    onOTPCodeChange = viewModel::onOTPCodeChange,
                    onVerifyOTPClick = viewModel::verifyOTP,
                    newPassword = uiState.newPassword,
                    onNewPasswordChange = viewModel::onNewPasswordChange,
                    confirmPassword = uiState.confirmPassword,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                    onResetPasswordClick = viewModel::resetPassword,
                    onLoginClick = onNavigateToLogin,
                    isLoading = uiState.isLoading,
                    isResending = uiState.isResending,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError,
                    otpError = uiState.otpError ?: "",
                    passwordError = uiState.passwordError,
                    confirmPasswordError = uiState.confirmPasswordError,
                    canResend = uiState.canResend,
                    resendCountdown = uiState.resendCountdown
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
