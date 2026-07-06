package com.m7md7sn.dentary.presentation.ui.patients

import com.m7md7sn.dentary.data.model.Patient

data class PatientsUiState(
    val patients: List<Patient> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedFilters: Set<String> = emptySet(),
    val showFilterDialog: Boolean = false,
    val availableFilters: List<String> = listOf(
        "حشو عادي",
        "جراحة",
        "تنظيف جير",
        "حشو عصب",
        "تركيبات متحركة",
        "تركيبات ثابتة"
    )
)
