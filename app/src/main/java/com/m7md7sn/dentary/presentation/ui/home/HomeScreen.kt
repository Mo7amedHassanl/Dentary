package com.m7md7sn.dentary.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.home.compoenents.HomeContent

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPatients: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HomeContent(
                    name = uiState.userName,
                    clinicName = uiState.clinicName,
                    recentPatientsList = uiState.recentPatients,
                    onPatientSeeAllClick = {
                        viewModel.onSeeAllPatientsClick(onNavigateToPatients)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreviewEn() {
    DentaryTheme {
        HomeScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun HomeScreenPreviewAr() {
    DentaryTheme {
        HomeScreen()
    }
}