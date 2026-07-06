package com.m7md7sn.dentary.presentation.ui.addpatient

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.R
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
        _uiState.value = _uiState.value.copy(
            fullName = newValue, 
            isFullNameError = false, 
            fullNameErrorMessage = null,
            isDirty = true
        )
    }

    fun onAgeChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            age = newValue, 
            isAgeError = false, 
            ageErrorMessage = null,
            isDirty = true
        )
    }

    fun onPhoneNumberChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = newValue, 
            isPhoneNumberError = false, 
            phoneNumberErrorMessage = null,
            isDirty = true
        )
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            email = newValue, 
            isEmailError = false, 
            emailErrorMessage = null,
            isDirty = true
        )
    }

    fun onGenderChange(newValue: String) {
        _uiState.value = _uiState.value.copy(gender = newValue, isGenderError = false, isDirty = true)
    }

    fun onAddressChange(newValue: String) {
        _uiState.value = _uiState.value.copy(
            address = newValue, 
            isAddressError = false, 
            addressErrorMessage = null,
            isDirty = true
        )
    }

    fun onMedicalProcedureChange(newValue: String) {
        _uiState.value = _uiState.value.copy(medicalProcedure = newValue, isProcedureError = false, isDirty = true)
    }

    fun uploadPatientImage(imageUri: Uri) {
        if (_uiState.value.isImageUploading) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isImageUploading = true, isDirty = true)
            val result = patientImageManager.uploadPatientImage(imageUri)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        patientImageUrl = result.data,
                        isImageUploading = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(isImageUploading = false)
                    _snackbarMessage.emit(Event(result.message ?: result.error.asUiText()))
                }
                else -> {
                    _uiState.value = _uiState.value.copy(isImageUploading = false)
                }
            }
        }
    }

    fun onCancelClick() {
        if (_uiState.value.isDirty) {
            _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = true)
        } else {
            viewModelScope.launch {
                _navigationEvent.emit(Event(NavigationEvent.GoBack))
            }
        }
    }
    
    fun dismissUnsavedChangesDialog() {
        _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = false)
    }
    
    fun confirmDiscardChanges() {
        _uiState.value = _uiState.value.copy(showUnsavedChangesDialog = false, isDirty = false)
        viewModelScope.launch {
            _navigationEvent.emit(Event(NavigationEvent.GoBack))
        }
    }

    fun addPatient() {
        if (_uiState.value.isLoading || _uiState.value.isImageUploading || !validateInputs()) {
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
                address = null, // Address field removed from UI
                medicalHistory = null,
                medicalProcedure = _uiState.value.medicalProcedure.trim().takeIf { it.isNotEmpty() },
                image = _uiState.value.patientImageUrl,
                gender = _uiState.value.gender
            )

            val result = patientRepository.createPatient(patient)
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true, 
                        showSuccessDialog = true, 
                        createdPatientId = result.data.id,
                        isDirty = false,
                        fullName = "",
                        age = "",
                        phoneNumber = "",
                        email = "",
                        gender = "",
                        address = "",
                        medicalProcedure = "",
                        patientImageUrl = null
                    )
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
            _uiState.value = _uiState.value.copy(
                isFullNameError = true, 
                fullNameErrorMessage = R.string.error_name_required
            )
            isValid = false
        }

        if (state.age.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isAgeError = true,
                ageErrorMessage = R.string.error_invalid_age
            )
            isValid = false
        } else if (state.age.toIntOrNull() == null) {
            _uiState.value = _uiState.value.copy(
                isAgeError = true, 
                ageErrorMessage = R.string.error_invalid_age
            )
            isValid = false
        }

        if (state.phoneNumber.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isPhoneNumberError = true,
                phoneNumberErrorMessage = R.string.error_invalid_phone
            )
            isValid = false
        } else if (state.phoneNumber.length < 8) {
            _uiState.value = _uiState.value.copy(
                isPhoneNumberError = true,
                phoneNumberErrorMessage = R.string.error_invalid_phone
            )
            isValid = false
        }

        if (state.email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.value = _uiState.value.copy(
                isEmailError = true, 
                emailErrorMessage = R.string.error_invalid_email
            )
            isValid = false
        }
        
        if (state.gender.isBlank()) {
            _uiState.value = _uiState.value.copy(isGenderError = true)
            isValid = false
        }
        
        if (state.medicalProcedure.isBlank()) {
            _uiState.value = _uiState.value.copy(isProcedureError = true)
            isValid = false
        }

        return isValid
    }
    
    fun dismissSuccessDialog() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
        viewModelScope.launch {
            _navigationEvent.emit(Event(NavigationEvent.GoBack))
        }
    }
    
    fun onAddAnother() {
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
    }
    
    fun onViewPatient() {
        val patientId = _uiState.value.createdPatientId
        _uiState.value = _uiState.value.copy(showSuccessDialog = false)
        if (patientId != null) {
            viewModelScope.launch {
                _navigationEvent.emit(Event(NavigationEvent.NavigateToPatient(patientId)))
            }
        }
    }

    fun clearForm() {
        _uiState.value = _uiState.value.copy(
            fullName = "",
            age = "",
            phoneNumber = "",
            email = "",
            gender = "",
            address = "",
            medicalProcedure = "",
            patientImageUrl = null,
            isDirty = false
        )
    }

    fun clearFieldError(field: String) {
        when (field) {
            "fullName" -> _uiState.value = _uiState.value.copy(isFullNameError = false, fullNameErrorMessage = null)
            "age" -> _uiState.value = _uiState.value.copy(isAgeError = false, ageErrorMessage = null)
            "phoneNumber" -> _uiState.value = _uiState.value.copy(isPhoneNumberError = false, phoneNumberErrorMessage = null)
            "email" -> _uiState.value = _uiState.value.copy(isEmailError = false, emailErrorMessage = null)
        }
    }

    fun clearErrors() {
        _uiState.value = _uiState.value.copy(
            isFullNameError = false, fullNameErrorMessage = null,
            isAgeError = false, ageErrorMessage = null,
            isPhoneNumberError = false, phoneNumberErrorMessage = null,
            isEmailError = false, emailErrorMessage = null,
            isGenderError = false, isProcedureError = false
        )
    }
}

sealed class NavigationEvent {
    object GoBack : NavigationEvent()
    data class NavigateToPatient(val patientId: String) : NavigationEvent()
}