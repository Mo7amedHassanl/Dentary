package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R

@Composable
fun AddPatientContent(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    isFullNameError: Boolean,
    fullNameErrorMessage: Int? = null,
    age: String,
    onAgeChange: (String) -> Unit,
    isAgeError: Boolean,
    ageErrorMessage: Int? = null,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isPhoneNumberError: Boolean,
    phoneNumberErrorMessage: Int? = null,
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: Boolean,
    emailErrorMessage: Int? = null,
    address: String = "",
    onAddressChange: (String) -> Unit = {},
    isAddressError: Boolean = false,
    addressErrorMessage: Int? = null,
    gender: String,
    onGenderChange: (String) -> Unit,
    isGenderError: Boolean,
    medicalProcedure: String,
    onMedicalProcedureChange: (String) -> Unit,
    isProcedureError: Boolean,
    patientImageUrl: String?,
    onUpdatePatientImage: (android.net.Uri) -> Unit,
    isLoading: Boolean,
    isImageUploading: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    focusManager: FocusManager,
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AddPatientTitle(isEditMode = isEditMode)
        Spacer(modifier = Modifier.height(40.dp))
        AddPatientImage(
            patientImageUrl = patientImageUrl,
            isImageUploading = isImageUploading,
            onUpdatePatientImage = onUpdatePatientImage
        )
        Spacer(modifier = Modifier.height(28.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            if (isGenderError) {
                Text(
                    text = stringResource(R.string.error_gender_required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.height(16.dp)
                )
            } else {
                Spacer(Modifier.height(16.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
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
            isProcedureError = isProcedureError,
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(38.dp))
        AddPatientActionButtons(
            isLoading = isLoading,
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick,
            enabled = !isImageUploading
        )
    }
}
