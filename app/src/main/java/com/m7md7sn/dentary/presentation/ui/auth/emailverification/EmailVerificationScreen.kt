package com.m7md7sn.dentary.presentation.ui.auth.emailverification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.components.EmailVerificationContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterHeader
import com.m7md7sn.dentary.utils.Result
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationScreen(
    onVerificationSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EmailVerificationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.verificationResult) {
        when (uiState.verificationResult) {
            is Result.Success -> {
                onVerificationSuccess()
                viewModel.resetVerificationResult()
            }
            else -> { /* No action needed */ }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
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
                    .verticalScroll(scrollState),
            ) {
                RegisterHeader()
                EmailVerificationContent(
                    email = uiState.email,
                    otpCode = uiState.otpCode,
                    onOTPCodeChange = viewModel::onOTPCodeChange,
                    otpError = uiState.otpError ?: "",
                    onConfirmClick = viewModel::verifyOTP,
                    onResendClick = viewModel::resendVerificationCode,
                    isLoading = uiState.isLoading,
                    isResending = uiState.isResending,
                    canResend = uiState.canResend,
                    resendCountdown = uiState.resendCountdown,
                    onBackClick = onNavigateBack
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
private fun EmailVerificationScreenPreviewEn() {
    DentaryTheme {
        EmailVerificationScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun EmailVerificationScreenPreviewAr() {
    DentaryTheme {
        EmailVerificationScreen()
    }
}