package com.m7md7sn.dentary.presentation.ui.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.data.model.LoginCredentials
import com.m7md7sn.dentary.presentation.common.components.CommonButton
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.AuthUiState

@Composable
fun LoginContent(
    onForgetPasswordClick: () -> Unit,
    onCreateNewAccountClick: () -> Unit,
    onLoginClick: (LoginCredentials) -> Unit = {},
    uiState: AuthUiState,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Local validation states
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Validation function
    fun validateInputs(): Boolean {
        var isValid = true

        // Email validation
        emailError = when {
            email.isBlank() -> {
                isValid = false
                "Email is required"
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                isValid = false
                "Please enter a valid email address"
            }
            else -> null
        }

        // Password validation
        passwordError = when {
            password.isBlank() -> {
                isValid = false
                "Password is required"
            }
            password.length < 6 -> {
                isValid = false
                "Password must be at least 6 characters"
            }
            else -> null
        }

        return isValid
    }

    fun handleLogin() {
        if (validateInputs()) {
            onLoginClick(
                LoginCredentials(
                    email = email.trim(),
                    password = password
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 26.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier.padding(bottom = 18.dp),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlexandriaBlack,
                fontWeight = FontWeight.Black,
                lineHeight = 20.sp,
                color = DentaryBlue
            )
        )

        // Display server error if exists
        uiState.error?.let { error ->
            Text(
                text = error,
                modifier = Modifier.padding(bottom = 16.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = AlexandriaRegular,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            )
        }

        LoginTextFields(
            modifier = Modifier.fillMaxWidth(),
            email = email,
            onEmailChange = {
                email = it
                // Clear email error when user starts typing
                if (emailError != null) emailError = null
            },
            password = password,
            onPasswordChange = {
                password = it
                // Clear password error when user starts typing
                if (passwordError != null) passwordError = null
            },
            emailError = emailError,
            passwordError = passwordError,
            isEmailError = emailError != null,
            isPasswordError = passwordError != null,
            onDone = { handleLogin() },
            focusManager = LocalFocusManager.current,
            isEnabled = !uiState.isLoading
        )

        ForgetPasswordButton(
            onClick = onForgetPasswordClick,
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(8.dp))

        CommonButton(
            text = stringResource(R.string.join),
            onClick = { handleLogin() },
            isLoading = uiState.isLoading,
            enabled = !uiState.isLoading
        )

        Spacer(Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.have_no_account),
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
            onClick = onCreateNewAccountClick,
            enabled = !uiState.isLoading
        ) {
            Text(
                text = stringResource(R.string.create_new_account),
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

@Preview
@Composable
private fun LoginContentPrev() {
    LoginContent({}, {}, {}, AuthUiState())
}
