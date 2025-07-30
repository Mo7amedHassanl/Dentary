package com.m7md7sn.dentary.presentation.ui.profile

import com.m7md7sn.dentary.data.model.Profile
import com.m7md7sn.dentary.data.model.MedicalProcedureStats

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val userEmail: String? = null,
    val totalPatients: Int = 0,
    val medicalProcedureStats: List<MedicalProcedureStats> = emptyList(),
    val error: String? = null,
    val isError: Boolean = false
)