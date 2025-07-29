package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen

@Composable
fun AccountSettings(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
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
            onClick = {
                onNavigateToScreen(SettingsScreen.EditDoctorAndClinicInfo)
            }
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.change_password),
            onClick = {
                onNavigateToScreen(SettingsScreen.ChangePassword)
            }
        )
    }
}

@Composable
fun DoctorAndClinicInfoSettings(
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
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
            icon = R.drawable.ic_user,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            ),

            )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "التخصص",
            icon = R.drawable.ic_specialization,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "Johnsondoe@nomail.com",
            icon = R.drawable.ic_email,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
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
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "0123456789",
            icon = R.drawable.ic_phone,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            placeholder = "المنطقة",
            icon = R.drawable.ic_location,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
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

@Composable
fun PasswordChangeSettings(
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        SectionTitle(
            title = R.string.change_password,
            titleIcon = R.drawable.ic_lock,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextFieldNoIcon(
            placeholder = stringResource(R.string.current_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextFieldNoIcon(
            placeholder = stringResource(R.string.new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(
                        FocusDirection.Down
                    )
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextFieldNoIcon(
            placeholder = stringResource(R.string.confirm_new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = { /* Handle save action */ },
            onCancelClick = { /* Handle cancel action */ }
        )
    }
}