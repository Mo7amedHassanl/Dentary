package com.m7md7sn.dentary.presentation.ui.patients

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientsContent

@Composable
fun PatientsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

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
                PatientsContent()
            }
        }
    }
}

@Preview
@Composable
private fun PatientsScreenPreviewEn() {
    DentaryTheme {
        PatientsScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun PatientsScreenPreviewAr() {
    DentaryTheme {
        PatientsScreen()
    }
}