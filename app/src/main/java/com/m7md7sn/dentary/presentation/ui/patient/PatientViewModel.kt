package com.m7md7sn.dentary.presentation.ui.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.domain.usecase.medicalhistory.DeleteMedicalHistoryUseCase
import com.m7md7sn.dentary.domain.usecase.medicalhistory.GetMedicalHistoriesUseCase
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val getMedicalHistoriesUseCase: GetMedicalHistoriesUseCase,
    private val deleteMedicalHistoryUseCase: DeleteMedicalHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    private val _patientId = MutableStateFlow<String?>(null)

    val medicalHistories: StateFlow<List<MedicalHistory>> = _patientId
        .flatMapLatest { id ->
            if (id != null) {
                getMedicalHistoriesUseCase(id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadPatient(patientId: String) {
        if (_uiState.value.isLoading) return

        _patientId.value = patientId
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

    fun toggleMedicalHistorySelection(id: String) {
        val currentSelected = _uiState.value.selectedMedicalHistoryIds.toMutableSet()
        if (currentSelected.contains(id)) {
            currentSelected.remove(id)
        } else {
            currentSelected.add(id)
        }
        _uiState.value = _uiState.value.copy(
            selectedMedicalHistoryIds = currentSelected,
            isSelectionMode = currentSelected.isNotEmpty()
        )
    }

    fun clearMedicalHistorySelection() {
        _uiState.value = _uiState.value.copy(
            selectedMedicalHistoryIds = emptySet(),
            isSelectionMode = false
        )
    }

    fun deleteSelectedMedicalHistories() {
        val selectedIds = _uiState.value.selectedMedicalHistoryIds
        if (selectedIds.isEmpty()) return
        viewModelScope.launch {
            selectedIds.forEach { id ->
                deleteMedicalHistoryUseCase(id)
            }
            clearMedicalHistorySelection()
        }
    }

    fun togglePatientDetailsListVisibility() {
        _uiState.value = _uiState.value.copy(
            isPatientDetailsListVisible = !_uiState.value.isPatientDetailsListVisible
        )
    }
}
