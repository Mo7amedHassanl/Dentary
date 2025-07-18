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
fun HomeContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            HomeHeader()
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
        item {
            PatientSearchBar()
        }
        item {
            Spacer(Modifier.height(20.dp))
        }
        item {
            RecentPatientsTitle()
        }
        item {
            Spacer(Modifier.height(12.dp))
        }

        // Recent patients items directly in the main LazyColumn
        items(
            listOf(
                Patient(
                    id = "1",
                    name = "علي حسن علي",
                    phoneNumber = "0123456789",
                    age = 15,
                    email = "hell",
                    lastVisitDate = "11-12-2025",
                ),
                Patient(
                    id = "2",
                    name = "محمد أحمد محمد",
                    phoneNumber = "0987654321",
                    age = 30,
                    email = "",
                    lastVisitDate = "10-11-2025",
                ),
                Patient(
                    id = "3",
                    name = "علي حسن علي",
                    phoneNumber = "0123456789",
                    age = 15,
                    email = "hell",
                    lastVisitDate = "11-12-2025",
                ),
                Patient(
                    id = "4",
                    name = "محمد أحمد محمد",
                    phoneNumber = "0987654321",
                    age = 30,
                    email = "",
                    lastVisitDate = "10-11-2025",
                )
            )
        ) { patient ->
            PatientItem(patient = patient)
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
        item {
            HomeAppointments()
        }
    }
}
