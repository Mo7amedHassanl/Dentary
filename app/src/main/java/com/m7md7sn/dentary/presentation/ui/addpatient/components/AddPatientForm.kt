package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.theme.DentaryLightGray

@Composable
fun AddPatientForm(
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
    address: String,
    onAddressChange: (String) -> Unit,
    isAddressError: Boolean,
    addressErrorMessage: String? = null,
    medicalProcedure: String,
    onMedicalProcedureChange: (String) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AddPatientTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = stringResource(R.string.full_name),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            isError = isFullNameError,
            errorMessage = fullNameErrorMessage,
        )
        AddPatientTextField(
            value = age,
            onValueChange = onAgeChange,
            placeholder = stringResource(R.string.age),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            isError = isAgeError,
            errorMessage = ageErrorMessage,
        )
        AddPatientTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            placeholder = stringResource(R.string.phone_number),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            isError = isPhoneNumberError,
            errorMessage = phoneNumberErrorMessage,
        )
        AddPatientTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.email_optional),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            isError = isEmailError,
            errorMessage = emailErrorMessage,
        )
        MedicalProcedureDropdown(
            value = medicalProcedure,
            onValueChange = onMedicalProcedureChange
        )
    }
}

@Preview
@Composable
fun AddPatientTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "البريد الإلكتروني",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = DentaryBlue,
            focusedTextColor = DentaryBlue,
            unfocusedTextColor = DentaryBlueGray,
            disabledTextColor = DentaryBlueGray,
            disabledContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledBorderColor = DentaryBlueGray,
            focusedBorderColor = DentaryBlue,
            unfocusedBorderColor = DentaryBlueGray,
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isError) Color.Red else DentaryBlueGray,
                    fontFamily = AlexandriaMedium
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontFamily = AlexandriaMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                )
            }
        } else null,
        visualTransformation = visualTransformation,
        enabled = enabled,
        shape = CircleShape,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) Color.Red else DentaryBlue,
            fontFamily = AlexandriaMedium
        ),
    )
}