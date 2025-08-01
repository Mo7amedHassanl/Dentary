package com.m7md7sn.dentary.presentation.ui.auth.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
fun LoginTextFields(
    focusManager: FocusManager,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onDone: () -> Unit,
    emailError: String? = null,
    passwordError: String? = null,
    isEmailError: Boolean = false,
    isPasswordError: Boolean = false,
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = { }
) {
    CommonTextField(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(id = R.string.email),
        trailingIcon = painterResource(id = R.drawable.ic_user),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        isError = isEmailError,
        errorMessage = emailError,
        modifier = modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(16.dp))
    CommonTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(id = R.string.password),
        trailingIcon = rememberVectorPainter(
            image = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        isError = isPasswordError,
        errorMessage = passwordError,
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onTrailingIconClick = onTogglePasswordVisibility
    )
}