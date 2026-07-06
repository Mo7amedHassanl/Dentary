package com.m7md7sn.dentary.presentation.ui.patients

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.patients.components.FilterPickerDialog
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientsContent

@Composable
fun PatientsScreen(
    modifier: Modifier = Modifier,
    initialSearchQuery: String? = null,
    viewModel: PatientsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToPatient: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialSearchQuery) {
        initialSearchQuery?.let { viewModel.searchPatients(it) }
    }

    if (uiState.showFilterDialog) {
        FilterPickerDialog(
            selectedFilters = uiState.selectedFilters,
            onToggleFilter = viewModel::toggleFilter,
            onDismiss = { viewModel.toggleFilterDialog(false) }
        )
    }

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
                selectedFilters = uiState.selectedFilters,
                onSearchQueryChange = viewModel::searchPatients,
                onFilterClick = { viewModel.toggleFilterDialog(true) },
                onFilterRemove = viewModel::removeFilter,
                onClearFilters = viewModel::clearFilters,
                onNavigateBack = onNavigateBack,
                onRefresh = viewModel::refreshPatients,
                onPatientClick = { patient ->
                    patient.id?.let { onNavigateToPatient(it) }
                }
            )
        }
    }
}
