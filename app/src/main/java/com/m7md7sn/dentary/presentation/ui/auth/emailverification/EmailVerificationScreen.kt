package com.m7md7sn.dentary.presentation.ui.auth.emailverification

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
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.components.EmailVerificationContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterContent
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.RegisterHeader

@Composable
fun EmailVerificationScreen(modifier: Modifier = Modifier) {
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
            EmailVerificationContent()
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