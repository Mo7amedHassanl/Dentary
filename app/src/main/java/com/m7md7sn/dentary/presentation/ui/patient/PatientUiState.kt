package com.m7md7sn.dentary.presentation.ui.patient

import com.m7md7sn.dentary.data.model.Patient

data class PatientUiState(
    val isLoading: Boolean = false,
    val patient: Patient? = null,
    val error: String? = null,
    val isPatientDetailsListVisible: Boolean = false,
)