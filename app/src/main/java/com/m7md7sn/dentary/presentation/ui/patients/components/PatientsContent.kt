package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.data.model.Patient
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun PatientsContent(
    modifier: Modifier = Modifier,
    patients: List<Patient> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    searchQuery: String = "",
    selectedFilters: Set<String> = emptySet(),
    onSearchQueryChange: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onFilterRemove: (String) -> Unit = {},
    onClearFilters: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onPatientClick: (Patient) -> Unit = {},
    selectedPatientIds: Set<String> = emptySet(),
    isSelectionMode: Boolean = false,
    onPatientLongClick: (Patient) -> Unit = {},
    onToggleSelection: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PatientsTitle()
            }
        }
        item {
            Spacer(Modifier.height(20.dp))
        }
        item {
            PatientSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                showFilterButton = patients.isNotEmpty() || selectedFilters.isNotEmpty() || searchQuery.isNotEmpty(),
                onFilterClick = onFilterClick
            )
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
        item {
            PatientSearchFilters(
                selectedFilters = selectedFilters,
                onFilterRemove = onFilterRemove,
                onClearAll = onClearFilters
            )
        }
        item {
            Spacer(Modifier.height(20.dp))
        }

        if (isLoading && patients.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(patients, key = { it.id }) { patient ->
                PatientItem(
                    patient = patient,
                    isSelected = selectedPatientIds.contains(patient.id),
                    isSelectionMode = isSelectionMode,
                    onClick = {
                        if (isSelectionMode) {
                            onToggleSelection(patient.id)
                        } else {
                            onPatientClick(patient)
                        }
                    },
                    onLongClick = { onPatientLongClick(patient) }
                )
            }
        }
    }
}
