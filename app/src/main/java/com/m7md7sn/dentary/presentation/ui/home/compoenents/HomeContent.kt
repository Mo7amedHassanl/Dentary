package com.m7md7sn.dentary.presentation.ui.home.compoenents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientList
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientSearchBar
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientSearchFilters
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientsTitle

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeHeader()
        Spacer(Modifier.height(20.dp))
        PatientSearchBar()
        Spacer(Modifier.height(20.dp))
        RecentPatientsList()
        Spacer(Modifier.height(16.dp))
        HomeAppointments()
    }
}
