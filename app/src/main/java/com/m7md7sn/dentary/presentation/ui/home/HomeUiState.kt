package com.m7md7sn.dentary.presentation.ui.home

import com.m7md7sn.dentary.data.model.Patient

data class HomeUiState(
    val userName: String = "",
    val clinicName: String = "",
    val profilePictureUrl: String? = null,
    val recentPatients: List<Patient> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
