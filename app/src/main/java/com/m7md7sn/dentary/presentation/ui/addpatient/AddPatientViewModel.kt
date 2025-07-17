package com.m7md7sn.dentary.presentation.ui.addpatient

import androidx.lifecycle.ViewModel
import com.m7md7sn.dentary.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(AddPatientsUiState())
    val uiState: StateFlow<AddPatientsUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    fun onFullNameChange(fullName: String) {
        _uiState.value = _uiState.value.copy(fullName = fullName,)
    }

    fun onAgeChange(age: String) {
        _uiState.value = _uiState.value.copy(age = age,)
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber,)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email,)
    }
}