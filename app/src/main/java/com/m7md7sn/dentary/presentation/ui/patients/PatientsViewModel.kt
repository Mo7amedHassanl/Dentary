package com.m7md7sn.dentary.presentation.ui.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.domain.usecase.patient.GetPatientsUseCase
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val getPatientsUseCase: GetPatientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState.asStateFlow()

    private var patientsJob: Job? = null
    private var allPatientsFromRepo: List<Patient> = emptyList()

    init {
        observePatients()
        refreshPatients()
    }

    private fun observePatients(query: String = "") {
        patientsJob?.cancel()
        patientsJob = viewModelScope.launch {
            getPatientsUseCase(query = query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        allPatientsFromRepo = result.data
                        applyFilters()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = result.message ?: result.error.asUiText(),
                            isLoading = false
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun applyFilters() {
        val selected = _uiState.value.selectedFilters
        val filtered = if (selected.isEmpty()) {
            allPatientsFromRepo
        } else {
            allPatientsFromRepo.filter { patient ->
                patient.medicalProcedure in selected
            }
        }
        _uiState.value = _uiState.value.copy(patients = filtered)
    }

    fun searchPatients(query: String) {
        if (_uiState.value.searchQuery == query) return
        _uiState.value = _uiState.value.copy(searchQuery = query)
        observePatients(query)
    }

    fun toggleFilterDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showFilterDialog = show)
    }

    fun toggleFilter(filter: String) {
        val current = _uiState.value.selectedFilters.toMutableSet()
        if (current.contains(filter)) {
            current.remove(filter)
        } else {
            current.add(filter)
        }
        _uiState.value = _uiState.value.copy(selectedFilters = current)
        applyFilters()
    }
    
    fun removeFilter(filter: String) {
        val current = _uiState.value.selectedFilters.toMutableSet()
        current.remove(filter)
        _uiState.value = _uiState.value.copy(selectedFilters = current)
        applyFilters()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(selectedFilters = emptySet())
        applyFilters()
    }

    fun refreshPatients() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getPatientsUseCase.refresh()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
        observePatients("")
    }
}