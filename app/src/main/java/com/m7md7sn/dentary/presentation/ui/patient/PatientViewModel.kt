package com.m7md7sn.dentary.presentation.ui.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    fun loadPatient(patientId: String) {
        if (_uiState.value.isLoading) return
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = patientRepository.getPatientById(patientId)
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(patient = result.data, error = null)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(error = result.message ?: result.error.asUiText())
                }
                is Result.Loading -> {
                }
            }
        }
    }

    fun togglePatientDetailsListVisibility() {
        _uiState.value = _uiState.value.copy(
            isPatientDetailsListVisible = !_uiState.value.isPatientDetailsListVisible
        )
    }
}