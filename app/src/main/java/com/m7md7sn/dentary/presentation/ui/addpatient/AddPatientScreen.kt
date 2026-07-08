package com.m7md7sn.dentary.presentation.ui.addpatient

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.addpatient.components.AddPatientContent
import kotlinx.coroutines.launch

@Composable
fun AddPatientScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPatientViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToPatient: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Handle system back button/gesture
    BackHandler(enabled = uiState.isDirty) {
        viewModel.onCancelClick()
    }

    // Handle snackbar messages
    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    // Handle navigation events
    LaunchedEffect(viewModel.navigationEvent) {
        viewModel.navigationEvent.collect { event ->
            event.getContentIfNotHandled()?.let { navigationEvent ->
                when (navigationEvent) {
                    is NavigationEvent.GoBack -> {
                        onNavigateBack()
                    }
                    is NavigationEvent.NavigateToPatient -> {
                        onNavigateToPatient(navigationEvent.patientId)
                    }
                }
            }
        }
    }

    // Unsaved Changes Dialog
    if (uiState.showUnsavedChangesDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissUnsavedChangesDialog() },
            title = {
                Text(
                    text = stringResource(R.string.unsaved_changes_title),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.unsaved_changes_message),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmDiscardChanges() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.discard),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = AlexandriaRegular,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.dismissUnsavedChangesDialog() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DentaryBlue
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.stay),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = AlexandriaRegular,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            },
            containerColor = BackgroundColor,
            titleContentColor = DentaryDarkBlue,
            textContentColor = DentaryBlue,
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        )
    }

    // Success Dialog
    if (uiState.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSuccessDialog() },
            title = {
                Text(
                    text = stringResource(R.string.success),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                Text(
                    text = if (uiState.isEditMode) stringResource(R.string.success) else stringResource(R.string.patient_added_successfully),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                    )
                )
            },
            confirmButton = {
                Column(horizontalAlignment = Alignment.End) {
                    Button(
                        onClick = { viewModel.onViewPatient() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DentaryBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.view_patient),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = AlexandriaRegular,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    Button(
                        onClick = { viewModel.onAddAnother() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5F67EC),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_another),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = AlexandriaRegular,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                    TextButton(onClick = { viewModel.dismissSuccessDialog() }) {
                        Text(
                            text = stringResource(R.string.back_to_list),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = AlexandriaRegular,
                                fontWeight = FontWeight.Medium,
                                color = DentaryBlue
                            )
                        )
                    }
                }
            },
            containerColor = BackgroundColor,
            titleContentColor = DentaryDarkBlue,
            textContentColor = DentaryBlue,
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            }
        )
    }

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AddPatientContent(
                    fullName = uiState.fullName,
                    onFullNameChange = viewModel::onFullNameChange,
                    isFullNameError = uiState.isFullNameError,
                    fullNameErrorMessage = uiState.fullNameErrorMessage,
                    age = uiState.age,
                    onAgeChange = viewModel::onAgeChange,
                    isAgeError = uiState.isAgeError,
                    ageErrorMessage = uiState.ageErrorMessage,
                    phoneNumber = uiState.phoneNumber,
                    onPhoneNumberChange = viewModel::onPhoneNumberChange,
                    isPhoneNumberError = uiState.isPhoneNumberError,
                    phoneNumberErrorMessage = uiState.phoneNumberErrorMessage,
                    email = uiState.email,
                    onEmailChange = viewModel::onEmailChange,
                    isEmailError = uiState.isEmailError,
                    emailErrorMessage = uiState.emailErrorMessage,
                    address = uiState.address,
                    onAddressChange = viewModel::onAddressChange,
                    isAddressError = uiState.isAddressError,
                    addressErrorMessage = uiState.addressErrorMessage,
                    gender = uiState.gender,
                    onGenderChange = viewModel::onGenderChange,
                    isGenderError = uiState.isGenderError,
                    medicalProcedure = uiState.medicalProcedure,
                    onMedicalProcedureChange = viewModel::onMedicalProcedureChange,
                    isProcedureError = uiState.isProcedureError,
                    patientImageUrl = uiState.patientImageUrl,
                    onUpdatePatientImage = viewModel::uploadPatientImage,
                    isLoading = uiState.isLoading,
                    isImageUploading = uiState.isImageUploading,
                    onSaveClick = viewModel::addPatient,
                    onCancelClick = viewModel::onCancelClick,
                    focusManager = focusManager,
                    isEditMode = uiState.isEditMode
                )
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

    }
}

@Preview
@Composable
private fun AddPatientScreenPreviewEn() {
    DentaryTheme {
        AddPatientScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun AddPatientScreenPreviewAr() {
    DentaryTheme {
        AddPatientScreen()
    }
}
