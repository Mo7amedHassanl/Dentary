package com.m7md7sn.dentary.presentation.ui.patients

import com.m7md7sn.dentary.data.model.Patient

enum class PatientSortOrder {
    NAME,
    LAST_UPDATE
}

data class PatientsUiState(
    val patients: List<Patient> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedFilters: Set<String> = emptySet(),
    val showFilterDialog: Boolean = false,
    val sortOrder: PatientSortOrder = PatientSortOrder.NAME,
    val selectedPatientIds: Set<String> = emptySet(),
    val isSelectionMode: Boolean = false
)
