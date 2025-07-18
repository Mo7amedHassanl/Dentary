package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
    onSearchQueryChange: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            PatientsTitle()
        }
        item {
            Spacer(Modifier.height(20.dp))
        }
        item {
            PatientSearchBar(
//                query = searchQuery,
//                onQueryChange = onSearchQueryChange
            )
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
        item {
            PatientSearchFilters()
        }
        item {
            Spacer(Modifier.height(20.dp))
        }

        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        } else {
            items(patients) { patient ->
                PatientItem(patient = patient)
            }
        }
    }
}
