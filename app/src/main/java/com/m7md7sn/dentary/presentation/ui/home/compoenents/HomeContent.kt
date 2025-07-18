package com.m7md7sn.dentary.presentation.ui.home.compoenents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientItem
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientSearchBar

@Composable
fun HomeContent(
    name: String,
    clinicName: String,
    recentPatientsList: List<Patient>,
    onPatientSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            HomeHeader(
                name = name,
                clinicName = clinicName,
            )
            Spacer(Modifier.height(8.dp))
        }
        item {
            PatientSearchBar()
            Spacer(Modifier.height(20.dp))
        }
        item {
            RecentPatientsTitle(
                onPatientSeeAllClick = onPatientSeeAllClick
            )
            Spacer(Modifier.height(12.dp))
        }
        items(
            recentPatientsList
        ) { patient ->
            PatientItem(patient = patient)
        }
        item {
            Spacer(Modifier.height(16.dp))
            HomeAppointments()
        }
    }
}
