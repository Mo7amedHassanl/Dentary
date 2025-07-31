package com.m7md7sn.dentary.presentation.ui.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.ui.patient.components.PatientInformationCard

@Composable
fun PatientContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp)
    ) {
        PatientInformationCard()
    }
}