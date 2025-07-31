package com.m7md7sn.dentary.presentation.ui.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginHeader
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.m7md7sn.dentary.utils.Result

@Composable
fun LoginScreen(
    onForgetPasswordClick: () -> Unit,
    onCreateNewAccountClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.loginResult) {
        when (uiState.loginResult) {
            is Result.Success -> {
                onLoginSuccess()
                viewModel.resetLoginResult()
            }

            is Result.Error -> {
                viewModel.resetLoginResult()
            }

            else -> {}
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
                    .verticalScroll(scrollState, enabled = true),
            ) {
                LoginHeader(
                    modifier = Modifier.weight(1f)
                )
                LoginContent(
                    modifier = Modifier.weight(1f),
                    onCreateNewAccountClick = onCreateNewAccountClick,
                    onForgetPasswordClick = onForgetPasswordClick,
                    email = uiState.email,
                    onEmailValueChange = viewModel::onEmailChange,
                    password = uiState.password,
                    onPasswordValueChange = viewModel::onPasswordChange,
                    onLoginClick = viewModel::login,
                    isLoading = uiState.isLoading,
                    isEmailError = uiState.emailError != null,
                    emailErrorMessage = uiState.emailError,
                    isPasswordError = uiState.passwordError != null,
                    passwordErrorMessage = uiState.passwordError,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                )
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F7FD)
@Composable
private fun LoginScreenPreviewEn() {
    DentaryTheme {
        LoginScreen({}, {}, {})
    }
}

@Preview(locale = "ar", showBackground = true, backgroundColor = 0xFFF5F7FD)
@Composable
private fun LoginScreenPreviewAr() {
    DentaryTheme {
        LoginScreen({}, {}, {})
    }
}