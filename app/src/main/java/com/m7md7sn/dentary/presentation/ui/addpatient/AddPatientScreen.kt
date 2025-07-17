package com.m7md7sn.dentary.presentation.ui.addpatient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.addpatient.components.AddPatientContent

@Composable
fun AddPatientScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPatientViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true)
                    .padding(vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AddPatientContent(
                    fullName = uiState.value.fullName,
                    onFullNameChange = viewModel::onFullNameChange,
                    isFullNameError = uiState.value.isFullNameError,
                    fullNameErrorMessage = uiState.value.fullNameErrorMessage,
                    age = uiState.value.age,
                    onAgeChange = viewModel::onAgeChange,
                    isAgeError = uiState.value.isAgeError,
                    ageErrorMessage = uiState.value.ageErrorMessage,
                    phoneNumber = uiState.value.phoneNumber,
                    onPhoneNumberChange = viewModel::onPhoneNumberChange,
                    isPhoneNumberError = uiState.value.isPhoneNumberError,
                    phoneNumberErrorMessage = uiState.value.phoneNumberErrorMessage,
                    email = uiState.value.email,
                    onEmailChange = viewModel::onEmailChange,
                    isEmailError = uiState.value.isEmailError,
                    emailErrorMessage = uiState.value.emailErrorMessage,
                    focusManager = focusManager
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