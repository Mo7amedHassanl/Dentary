package com.m7md7sn.dentary.presentation.ui.addpatient

data class AddPatientsUiState(
    val fullName: String = "",
    val age: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val address: String = "",
    val medicalProcedure: String = "",
    val patientImageUrl: String? = null,
    val isLoading: Boolean = false,
    val isImageUploading: Boolean = false,
    val isSuccess: Boolean = false,
    val isDirty: Boolean = false,
    val showUnsavedChangesDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val createdPatientId: String? = null,
    val isEditMode: Boolean = false,
    
    // Inline Errors
    val isFullNameError: Boolean = false,
    val fullNameErrorMessage: Int? = null,
    val isAgeError: Boolean = false,
    val ageErrorMessage: Int? = null,
    val isPhoneNumberError: Boolean = false,
    val phoneNumberErrorMessage: Int? = null,
    val isEmailError: Boolean = false,
    val emailErrorMessage: Int? = null,
    val isAddressError: Boolean = false,
    val addressErrorMessage: Int? = null,
    val isGenderError: Boolean = false,
    val isProcedureError: Boolean = false,
)
