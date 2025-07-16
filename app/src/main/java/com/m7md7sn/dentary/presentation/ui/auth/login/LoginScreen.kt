package com.m7md7sn.dentary.presentation.ui.auth.login

import android.graphics.Color
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.auth.AuthViewModel
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginHeader

@Composable
fun LoginScreen(
    onForgetPasswordClick: () -> Unit,
    onCreateNewAccountClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle successful authentication
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onLoginSuccess()
        }
    }

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            LoginHeader(
                modifier = Modifier.weight(1f)
            )
            LoginContent(
                modifier = Modifier.weight(1f),
                onCreateNewAccountClick = onCreateNewAccountClick,
                onForgetPasswordClick = onForgetPasswordClick,
                onLoginClick = { credentials ->
                    viewModel.login(credentials)
                },
                uiState = uiState
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