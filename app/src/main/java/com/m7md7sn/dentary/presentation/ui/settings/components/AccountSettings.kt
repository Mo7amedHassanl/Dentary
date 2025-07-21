package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle

@Composable
fun AccountSettings(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.account_settings),
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
            )
        )
        Spacer(Modifier.height(16.dp))
        SettingsItem(
            text = stringResource(R.string.edit_doctor_clinic_info),
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.change_password),
        )
    }
}

@Composable
fun DoctorAndClinicInfoSettings(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        SectionTitle(
            title = R.string.doctor_info,
            titleIcon = R.drawable.ic_doctor,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextField(
            value = "علي حسن علي محمد",
            icon = R.drawable.ic_user
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "التخصص",
            icon = R.drawable.ic_specialization
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "Johnsondoe@nomail.com",
            icon = R.drawable.ic_email
        )
        Spacer(Modifier.height(22.dp))
        SectionTitle(
            title = R.string.clinic_information,
            titleIcon = R.drawable.ic_clinic,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextField(
            value = "مركز دنتاري لطب الأسنان",
            icon = R.drawable.ic_clinic_name,
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "0123456789",
            icon = R.drawable.ic_phone,
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "المنطقة",
            icon = R.drawable.ic_location,
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "شعار العيادة",
            icon = R.drawable.ic_clinic_logo,
            modifier = Modifier.height(66.dp)
        )
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = { /* Handle save action */ },
            onCancelClick = { /* Handle cancel action */ }
        )
    }
}