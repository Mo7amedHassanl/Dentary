package com.m7md7sn.dentary.presentation.ui.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.splash.components.AnimatedLogo

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DentaryBlue),
        contentAlignment = Alignment.Center
    ) {
        AnimatedLogo()
    }
}

@Preview
@Composable
private fun SplashScreenPreviewEn() {
    DentaryTheme {
        SplashScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun SplashScreenPreviewAr() {
    DentaryTheme {
        SplashScreen()
    }
}