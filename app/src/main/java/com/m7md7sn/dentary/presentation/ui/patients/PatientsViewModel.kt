package com.m7md7sn.dentary.presentation.ui.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientsUiState())
    val uiState: StateFlow<PatientsUiState> = _uiState.asStateFlow()

    private var allPatients = emptyList<com.m7md7sn.dentary.data.model.Patient>()

    init {
        loadAllPatients()
    }

    private fun loadAllPatients() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = patientRepository.getAllPatients()) {
                is Result.Success -> {
                    allPatients = result.data
                    _uiState.value = _uiState.value.copy(
                        patients = allPatients,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        patients = emptyList(),
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun searchPatients(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(patients = allPatients)
            return
        }

        viewModelScope.launch {
            when (val result = patientRepository.searchPatients(query)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        patients = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        patients = emptyList(),
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun refreshPatients() {
        loadAllPatients()
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            patients = allPatients
        )
    }
}
