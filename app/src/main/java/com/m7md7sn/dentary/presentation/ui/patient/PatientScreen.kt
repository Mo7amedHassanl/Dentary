package com.m7md7sn.dentary.presentation.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.patient.components.PatientHeader
import com.m7md7sn.dentary.presentation.ui.profile.components.PatientContent

@Composable
fun PatientScreen(
    patientId: String,
    modifier: Modifier = Modifier,
    viewModel: PatientViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val patient = uiState.patient

    // Load patient data when patientId changes
    LaunchedEffect(patientId) {
        viewModel.loadPatient(patientId)
    }

    val scrollState = rememberScrollState()
    Surface(
        color = DentaryLightBlue,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when {
                    uiState.isLoading -> CircularProgressIndicator()
                    uiState.error != null -> Text(
                        text = uiState.error ?: "Unknown error",
                        color = Color.Red
                    )
                    uiState.patient != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = DentaryLightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.patient_header_bg),
                                contentDescription = null
                            )
                            PatientHeader(
                                patientName = patient?.name ?: "Unknown Patient",
                                medicalCondition = patient?.medicalProcedure ?: "No Medical Condition",
                                imageUri = patient?.image,
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(
                                    color = BackgroundColor,
                                    shape = RoundedCornerShape(topEnd = 35.dp, topStart = 35.dp)
                                ),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            PatientContent(
                                patient = patient ?: Patient(
                                    id = "",
                                    name = "",
                                    email = "",
                                    address = "",
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun PatientScreenPreviewEn() {
    DentaryTheme {
        PatientScreen("patient123")
    }
}

@Preview(locale = "ar")
@Composable
private fun PatientScreenPreviewAr() {
    DentaryTheme {
        PatientScreen("patient123")
    }
}