package com.m7md7sn.dentary.presentation.ui.auth.register.compoenents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.common.components.CommonTextField

@Composable
fun RegisterInformationTextFields(
    modifier: Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    nameError: String,
    emailError: String,
    passwordError: String,
    confirmPasswordError: String,
    isNameError: Boolean,
    isEmailError: Boolean,
    isPasswordError: Boolean,
    isConfirmPasswordError: Boolean,
    isPasswordVisible: Boolean = false,
    isConfirmPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    onToggleConfirmPasswordVisibility: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CommonTextField(
            value = name,
            onValueChange = onNameChange,
            label = stringResource(id = R.string.username),
            isError = isNameError,
            errorMessage = nameError,
            trailingIcon = painterResource(id = R.drawable.ic_user),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CommonTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(id = R.string.email),
            isError = isEmailError,
            errorMessage = emailError,
            trailingIcon = painterResource(id = R.drawable.ic_email),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CommonTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(id = R.string.password),
            isError = isPasswordError,
            errorMessage = passwordError,
            trailingIcon = rememberVectorPainter(
                image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onTogglePasswordVisibility
        )
        CommonTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = stringResource(id = R.string.confirm_password),
            isError = isConfirmPasswordError,
            errorMessage = confirmPasswordError,
            trailingIcon = rememberVectorPainter(
                image = if (isConfirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            onTrailingIconClick = onToggleConfirmPasswordVisibility
        )
    }
}


@Composable
fun RegisterClinicInformationTextFields(
    modifier: Modifier,
    clinicName: String,
    onClinicNameChange: (String) -> Unit,
    clinicAddress: String,
    onClinicAddressChange: (String) -> Unit,
    clinicPhoneNumber: String,
    onClinicPhoneNumberChange: (String) -> Unit,
    clinicNameError: String,
    clinicAddressError: String,
    clinicPhoneNumberError: String,
    isClinicNameError: Boolean,
    isClinicAddressError: Boolean,
    isClinicPhoneNumberError: Boolean
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CommonTextField(
            value = clinicName,
            onValueChange = onClinicNameChange,
            label = stringResource(id = R.string.clinic_name),
            isError = isClinicNameError,
            errorMessage = clinicNameError,
            trailingIcon = painterResource(id = R.drawable.ic_clinic_name),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CommonTextField(
            value = clinicPhoneNumber,
            onValueChange = onClinicPhoneNumberChange,
            label = stringResource(id = R.string.clinic_phone_number),
            isError = isClinicPhoneNumberError,
            errorMessage = clinicPhoneNumberError,
            trailingIcon = painterResource(id = R.drawable.ic_phone),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )
        CommonTextField(
            value = clinicAddress,
            onValueChange = onClinicAddressChange,
            label = stringResource(id = R.string.clinic_address),
            isError = isClinicAddressError,
            errorMessage = clinicAddressError,
            trailingIcon = painterResource(id = R.drawable.ic_location),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(focusDirection = FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}