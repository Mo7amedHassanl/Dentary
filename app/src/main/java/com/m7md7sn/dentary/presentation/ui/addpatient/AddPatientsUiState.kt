package com.m7md7sn.dentary.presentation.ui.addpatient

data class AddPatientsUiState(
    val fullName: String = "",
    val age: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val isFullNameError: Boolean = false,
    val fullNameErrorMessage: String? = null,
    val isAgeError: Boolean = false,
    val ageErrorMessage: String? = null,
    val isPhoneNumberError: Boolean = false,
    val phoneNumberErrorMessage: String? = null,
    val isEmailError: Boolean = false,
    val emailErrorMessage: String? = null,
)
