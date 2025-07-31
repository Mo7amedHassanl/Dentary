package com.m7md7sn.dentary.presentation.ui.patients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientsContent

@Composable
fun PatientsScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PatientsContent(
                patients = uiState.patients,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::searchPatients,
                onNavigateBack = onNavigateBack,
                onRefresh = viewModel::refreshPatients
            )
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