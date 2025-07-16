package com.m7md7sn.dentary.presentation.ui.auth.register.compoenents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.common.components.CommonButton
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

@Composable
fun RegisterContent(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    email: String,
    onEmailValueChange: (String) -> Unit,
    username: String,
    onUsernameValueChange: (String) -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordValueChange: (String) -> Unit,
    isLoading: Boolean,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    isPasswordError: Boolean,
    passwordErrorMessage: String?,
    isConfirmPasswordError: Boolean,
    confirmPasswordErrorMessage: String?,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isConfirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    isUsernameError: Boolean,
    usernameErrorMessage: String?,
    clinicName: String,
    onClinicNameValueChange: (String) -> Unit,
    clinicAddress: String,
    onClinicAddressValueChange: (String) -> Unit,
    clinicPhone: String,
    onClinicPhoneValueChange: (String) -> Unit,
    isClinicNameError: Boolean,
    clinicNameErrorMessage: String?,
    isClinicAddressError: Boolean,
    clinicAddressErrorMessage: String?,
    isClinicPhoneError: Boolean,
    clinicPhoneErrorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 26.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.register),
            modifier = Modifier.padding(bottom = 24.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlexandriaBlack,
                fontWeight = FontWeight.Black,
                lineHeight = 20.sp,
                color = DentaryBlue
            )
        )
        RegisterSectionTitle(
            title = R.string.register_information,
            titleIcon = R.drawable.ic_lock_round,
        )
        Spacer(Modifier.height(8.dp))
        RegisterInformationTextFields(
            modifier = Modifier.fillMaxWidth(),
            name = username,
            onNameChange = onUsernameValueChange,
            email = email,
            onEmailChange = onEmailValueChange,
            password = password,
            onPasswordChange = onPasswordValueChange,
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = onConfirmPasswordValueChange,
            nameError = usernameErrorMessage ?: "",
            emailError = emailErrorMessage ?: "",
            passwordError = passwordErrorMessage ?: "",
            confirmPasswordError = confirmPasswordErrorMessage ?: "",
            isNameError = isUsernameError,
            isEmailError = isEmailError,
            isPasswordError = isPasswordError,
            isConfirmPasswordError = isConfirmPasswordError,
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            isConfirmPasswordVisible = isConfirmPasswordVisible,
            onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
        )
        Spacer(Modifier.height(24.dp))
        RegisterSectionTitle(
            title = R.string.clinic_information,
            titleIcon = R.drawable.ic_clinic,
        )
        Spacer(Modifier.height(8.dp))
        RegisterClinicInformationTextFields(
            modifier = Modifier.fillMaxWidth(),
            clinicName = clinicName,
            onClinicNameChange = onClinicNameValueChange,
            clinicAddress = clinicAddress,
            onClinicAddressChange = onClinicAddressValueChange,
            clinicPhoneNumber = clinicPhone,
            onClinicPhoneNumberChange = onClinicPhoneValueChange,
            clinicNameError = clinicNameErrorMessage ?: "",
            clinicAddressError = clinicAddressErrorMessage ?: "",
            clinicPhoneNumberError = clinicPhoneErrorMessage ?: "",
            isClinicNameError = isClinicNameError,
            isClinicAddressError = isClinicAddressError,
            isClinicPhoneNumberError = isClinicPhoneError,
        )
        Spacer(Modifier.height(24.dp))
        CommonButton(
            text = stringResource(R.string.register_confirm),
            onClick = onRegisterClick,
            isLoading = isLoading
        )
        Spacer(Modifier.height(18.dp))
        Text(
            text = stringResource(R.string.have_account),
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontFamily = AlexandriaRegular,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA2A2A2),
                textAlign = TextAlign.Center,
            )
        )
        TextButton(
            onClick = onLoginClick,
        ) {
            Text(
                text = stringResource(R.string.login),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = DentaryBlue,
                    textAlign = TextAlign.Center,
                )
            )
        }
    }
}

@Composable
fun RegisterSectionTitle(
    @StringRes title: Int,
    @DrawableRes titleIcon: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = titleIcon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(DentaryBlue),
            modifier = Modifier.height(25.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(title),
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
                color = DentaryBlue
            )
        )
    }
}