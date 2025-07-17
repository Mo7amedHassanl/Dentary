package com.m7md7sn.dentary.presentation.ui.patients.components

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PatientsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PatientsTitle()
        Spacer(Modifier.height(20.dp))
        PatientSearchBar()
        Spacer(Modifier.height(12.dp))
        PatientSearchFilters()
        Spacer(Modifier.height(20.dp))
        PatientList()
    }
}
