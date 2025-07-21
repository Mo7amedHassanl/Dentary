package com.m7md7sn.dentary.presentation.ui.profile

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.profile.components.ProfileContent

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
               ProfileContent()
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
private fun ProfileScreenPreviewEn() {
    DentaryTheme {
        ProfileScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun ProfileScreenPreviewAr() {
    DentaryTheme {
        ProfileScreen()
    }
}