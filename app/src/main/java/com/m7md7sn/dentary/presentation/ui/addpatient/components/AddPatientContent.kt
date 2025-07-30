package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.unit.dp

@Composable
fun AddPatientContent(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    isFullNameError: Boolean,
    fullNameErrorMessage: String? = null,
    age: String,
    onAgeChange: (String) -> Unit,
    isAgeError: Boolean,
    ageErrorMessage: String? = null,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isPhoneNumberError: Boolean,
    phoneNumberErrorMessage: String? = null,
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: Boolean,
    emailErrorMessage: String? = null,
    gender: String,
    onGenderChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    isAddressError: Boolean,
    addressErrorMessage: String? = null,
    medicalProcedure: String,
    onMedicalProcedureChange: (String) -> Unit,
    patientImageUrl: String?,
    onUpdatePatientImage: (android.net.Uri) -> Unit,
    isLoading: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddPatientTitle()
        Spacer(modifier = Modifier.height(40.dp))
        AddPatientImage(
            patientImageUrl = patientImageUrl,
            onUpdatePatientImage = onUpdatePatientImage
        )
        Spacer(modifier = Modifier.height(28.dp))
        GenderRadioButtons(
            selectedGender = when (gender) {
                "MALE" -> Gender.MALE
                "FEMALE" -> Gender.FEMALE
                else -> null
            },
            onGenderSelected = { selectedGender ->
                onGenderChange(selectedGender?.name ?: "")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        AddPatientForm(
            fullName = fullName,
            onFullNameChange = onFullNameChange,
            isFullNameError = isFullNameError,
            fullNameErrorMessage = fullNameErrorMessage,
            age = age,
            onAgeChange = onAgeChange,
            isAgeError = isAgeError,
            ageErrorMessage = ageErrorMessage,
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isPhoneNumberError = isPhoneNumberError,
            phoneNumberErrorMessage = phoneNumberErrorMessage,
            email = email,
            onEmailChange = onEmailChange,
            isEmailError = isEmailError,
            emailErrorMessage = emailErrorMessage,
            address = address,
            onAddressChange = onAddressChange,
            isAddressError = isAddressError,
            addressErrorMessage = addressErrorMessage,
            medicalProcedure = medicalProcedure,
            onMedicalProcedureChange = onMedicalProcedureChange,
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(38.dp))
        AddPatientActionButtons(
            isLoading = isLoading,
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick
        )
    }
}