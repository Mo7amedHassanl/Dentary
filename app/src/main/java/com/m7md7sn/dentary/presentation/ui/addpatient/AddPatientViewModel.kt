package com.m7md7sn.dentary.presentation.ui.addpatient

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.PatientImageManager
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.utils.Event
import com.m7md7sn.dentary.utils.Result
import com.m7md7sn.dentary.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val authRepository: AuthRepository,
    private val patientImageManager: PatientImageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPatientsUiState())
    val uiState: StateFlow<AddPatientsUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    private val _navigationEvent = MutableSharedFlow<Event<NavigationEvent>>()
    val navigationEvent: SharedFlow<Event<NavigationEvent>> = _navigationEvent.asSharedFlow()

    fun onFullNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(fullName = newValue, isFullNameError = false, fullNameErrorMessage = null)
    }

    fun onAgeChange(newValue: String) {
        _uiState.value = _uiState.value.copy(age = newValue, isAgeError = false, ageErrorMessage = null)
    }

    fun onPhoneNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = newValue, isPhoneNumberError = false, phoneNumberErrorMessage = null)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue, isEmailError = false, emailErrorMessage = null)
    }

    fun onGenderChange(newValue: String) {
        _uiState.value = _uiState.value.copy(gender = newValue)
    }

    fun onAddressChange(newValue: String) {
        _uiState.value = _uiState.value.copy(address = newValue, isAddressError = false, addressErrorMessage = null)
    }

    fun onMedicalProcedureChange(newValue: String) {
        _uiState.value = _uiState.value.copy(medicalProcedure = newValue)
    }

    fun uploadPatientImage(imageUri: Uri) {
        if (_uiState.value.isLoading) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = patientImageManager.uploadPatientImage(imageUri)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        patientImageUrl = result.data,
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _snackbarMessage.emit(Event(result.message ?: result.error.asUiText()))
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun onCancelClick() {
        viewModelScope.launch {
            _navigationEvent.emit(Event(NavigationEvent.GoBack))
        }
    }

    fun addPatient() {
        if (_uiState.value.isLoading || !validateInputs()) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()
            val userId = currentUser?.id

            if (userId == null) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _snackbarMessage.emit(Event("User not authenticated. Please log in again."))
                return@launch
            }

            val patient = Patient(
                id = "", 
                userId = userId,
                name = _uiState.value.fullName.trim(),
                phoneNumber = _uiState.value.phoneNumber.trim().takeIf { it.isNotEmpty() },
                email = _uiState.value.email.trim().takeIf { it.isNotEmpty() },
                age = _uiState.value.age.toIntOrNull(),
                address = _uiState.value.address.trim().takeIf { it.isNotEmpty() },
                medicalHistory = null,
                medicalProcedure = _uiState.value.medicalProcedure.trim().takeIf { it.isNotEmpty() },
                image = _uiState.value.patientImageUrl,
                gender = _uiState.value.gender
            )

            val result = patientRepository.createPatient(patient)
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(isSuccess = true)
                    _snackbarMessage.emit(Event("Patient added successfully"))
                    clearForm()
                }
                is Result.Error -> {
                    _snackbarMessage.emit(Event(result.message ?: result.error.asUiText()))
                }
                else -> {}
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val state = _uiState.value

        if (state.fullName.isBlank()) {
            _uiState.value = _uiState.value.copy(isFullNameError = true, fullNameErrorMessage = "Name is required")
            isValid = false
        }

        if (state.age.isNotBlank() && state.age.toIntOrNull() == null) {
            _uiState.value = _uiState.value.copy(isAgeError = true, ageErrorMessage = "Invalid age")
            isValid = false
        }

        if (state.email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.value = _uiState.value.copy(isEmailError = true, emailErrorMessage = "Invalid email format")
            isValid = false
        }

        return isValid
    }

    fun clearForm() {
        _uiState.value = AddPatientsUiState()
    }

    fun clearFieldError(field: String) {
        when (field) {
            "fullName" -> _uiState.value = _uiState.value.copy(isFullNameError = false, fullNameErrorMessage = null)
            "age" -> _uiState.value = _uiState.value.copy(isAgeError = false, ageErrorMessage = null)
            "phoneNumber" -> _uiState.value = _uiState.value.copy(isPhoneNumberError = false, phoneNumberErrorMessage = null)
            "email" -> _uiState.value = _uiState.value.copy(isEmailError = false, emailErrorMessage = null)
            "address" -> _uiState.value = _uiState.value.copy(isAddressError = false, addressErrorMessage = null)
        }
    }

    fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            isFullNameError = false, fullNameErrorMessage = null,
            isAgeError = false, ageErrorMessage = null,
            isPhoneNumberError = false, phoneNumberErrorMessage = null,
            isEmailError = false, emailErrorMessage = null,
            isAddressError = false, addressErrorMessage = null
        )
    }
}

sealed class NavigationEvent {
    object GoBack : NavigationEvent()
}