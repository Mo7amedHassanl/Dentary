package com.m7md7sn.dentary.presentation.ui.auth.register

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginContent
import com.m7md7sn.dentary.presentation.ui.auth.login.components.LoginHeader
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterHeader

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            RegisterHeader()
            RegisterContent(
                onLoginClick = onLoginClick,
                modifier = Modifier.fillMaxSize()
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