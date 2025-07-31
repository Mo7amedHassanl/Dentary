package com.m7md7sn.dentary.presentation.ui.settings.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsUiState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import com.m7md7sn.dentary.utils.Event
import androidx.compose.foundation.layout.fillMaxWidth
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsItem
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsTextField
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsTextFieldNoIcon

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
    uiState: SettingsUiState,
    onFullNameChange: (String) -> Unit,
    onSpecializationChange: (String) -> Unit,
    onClinicNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onClinicAddressChange: (String) -> Unit,
    onClinicLogoChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit = {},
    fetchProfile: () -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarMessageFlow: SharedFlow<Event<String>>,
) {
    LaunchedEffect(Unit) { fetchProfile() }
    LaunchedEffect(Unit) {
        snackbarMessageFlow.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        if (uiState.isProfileLoading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
        }
        if (uiState.profileError != null) {
            Text(text = uiState.profileError ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }
        SectionTitle(
            title = R.string.doctor_info,
            titleIcon = R.drawable.ic_doctor,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextField(
            value = uiState.fullName,
            onValueChange = onFullNameChange,
            icon = R.drawable.ic_user,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsSpecializationDropdown(
            value = uiState.specialization,
            onValueChange = onSpecializationChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            value = uiState.email,
            onValueChange = {},
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
            ),
            enabled = false
        )
        Spacer(Modifier.height(22.dp))
        SectionTitle(
            title = R.string.clinic_information,
            titleIcon = R.drawable.ic_clinic,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextField(
            value = uiState.clinicName,
            onValueChange = onClinicNameChange,
            icon = R.drawable.ic_clinic_name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            value = uiState.phoneNumber,
            onValueChange = onPhoneNumberChange,
            placeholder = "0123456789",
            icon = R.drawable.ic_phone,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            value = uiState.clinicAddress,
            onValueChange = onClinicAddressChange,
            placeholder = "المنطقة",
            icon = R.drawable.ic_location,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextField(
            value = uiState.clinicLogo,
            onValueChange = onClinicLogoChange,
            placeholder = "شعار العيادة",
            icon = R.drawable.ic_clinic_logo,
            modifier = Modifier.height(66.dp)
        )
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = onSaveClick,
            onCancelClick = onBackClick,
            enabled = !uiState.isProfileLoading,
            isLoading = uiState.isProfileLoading
        )
    }
}

@Composable
fun PasswordChangeSettings(
    focusManager: FocusManager,
    uiState: SettingsUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    validateNewPassword: () -> Boolean,
    changePassword: () -> Unit,
    clearPasswordFields: () -> Unit,
    clearPasswordChangeState: () -> Unit,
    onBackClick: () -> Unit = {},
    snackbarHostState: SnackbarHostState,
    snackbarMessageFlow: SharedFlow<Event<String>>,
) {
    rememberCoroutineScope()
    LaunchedEffect(Unit) {
        snackbarMessageFlow.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }
    LaunchedEffect(uiState.passwordChangeSuccess) {
        if (uiState.passwordChangeSuccess) {
            clearPasswordFields()
            clearPasswordChangeState()
        }
    }
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        SectionTitle(
            title = R.string.change_password,
            titleIcon = R.drawable.ic_lock,
        )
        Spacer(Modifier.height(20.dp))
        SettingsTextFieldNoIcon(
            value = uiState.currentPassword,
            onValueChange = onCurrentPasswordChange,
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
            value = uiState.newPassword,
            onValueChange = onNewPasswordChange,
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
            ),
            isError = uiState.newPasswordError != null,
            errorMessage = uiState.newPasswordError ?: ""
        )
        Spacer(Modifier.height(10.dp))
        SettingsTextFieldNoIcon(
            value = uiState.confirmNewPassword,
            onValueChange = onConfirmNewPasswordChange,
            placeholder = stringResource(R.string.confirm_new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError ?: ""
        )
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = {
                if (validateNewPassword() && !uiState.isPasswordChanging) {
                    changePassword()
                }
            },
            onCancelClick = {
                clearPasswordFields()
                clearPasswordChangeState()
                focusManager.clearFocus()
                onBackClick()
            },
            enabled = !uiState.isPasswordChanging && uiState.newPasswordError == null && uiState.confirmPasswordError == null,
            isLoading = uiState.isPasswordChanging
        )
    }
}