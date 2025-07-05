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
    onLoginClick: () -> Unit,
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
            name = "",
            onNameChange = {},
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            confirmPassword = "",
            onConfirmPasswordChange = {},
            nameError = "",
            emailError = "",
            passwordError = "",
            confirmPasswordError = "",
            isNameError = false,
            isEmailError = false,
            isPasswordError = false,
            isConfirmPasswordError = false,
        )
        Spacer(Modifier.height(12.dp))
        RegisterSectionTitle(
            title = R.string.clinic_information,
            titleIcon = R.drawable.ic_clinic,
        )
        Spacer(Modifier.height(8.dp))
        RegisterClinicInformationTextFields(
            modifier = Modifier.fillMaxWidth(),
            clinicName = "",
            onClinicNameChange = {},
            clinicAddress = "",
            onClinicAddressChange = {},
            clinicPhoneNumber = "",
            onClinicPhoneNumberChange = {},
            clinicNameError = "",
            clinicAddressError = "",
            clinicPhoneNumberError = "",
            isClinicNameError = false,
            isClinicAddressError = false,
            isClinicPhoneNumberError = false
        )
        Spacer(Modifier.height(12.dp))
        CommonButton(
            text = stringResource(R.string.register_confirm),
            onClick = {}
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
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
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