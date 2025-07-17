package com.m7md7sn.dentary.presentation.ui.auth.passwordreset.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.common.components.CommonButton
import com.m7md7sn.dentary.presentation.common.components.CommonTextField
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightGray
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.PasswordResetStep

@Composable
fun PasswordResetContent(
    modifier: Modifier = Modifier,
    currentStep: PasswordResetStep,
    email: String,
    onEmailValueChange: (String) -> Unit,
    onSendPasswordResetClick: () -> Unit,
    onResendPasswordResetClick: () -> Unit = {},
    otpCode: String,
    onOTPCodeChange: (String) -> Unit,
    onVerifyOTPClick: () -> Unit,
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    isResending: Boolean = false,
    isEmailError: Boolean,
    emailErrorMessage: String?,
    otpError: String = "",
    passwordError: String? = null,
    confirmPasswordError: String? = null,
    canResend: Boolean = true,
    resendCountdown: Int = 0
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 26.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentStep) {
            PasswordResetStep.EMAIL_INPUT -> {
                EmailInputStep(
                    email = email,
                    onEmailValueChange = onEmailValueChange,
                    onSendPasswordResetClick = onSendPasswordResetClick,
                    onLoginClick = onLoginClick,
                    isLoading = isLoading,
                    isEmailError = isEmailError,
                    emailErrorMessage = emailErrorMessage
                )
            }
            PasswordResetStep.OTP_VERIFICATION -> {
                OTPVerificationStep(
                    email = email,
                    otpCode = otpCode,
                    onOTPCodeChange = onOTPCodeChange,
                    onVerifyOTPClick = onVerifyOTPClick,
                    onResendClick = onResendPasswordResetClick,
                    onLoginClick = onLoginClick,
                    isLoading = isLoading,
                    isResending = isResending,
                    otpError = otpError,
                    canResend = canResend,
                    resendCountdown = resendCountdown
                )
            }
            PasswordResetStep.PASSWORD_CHANGE -> {
                PasswordChangeStep(
                    newPassword = newPassword,
                    onNewPasswordChange = onNewPasswordChange,
                    confirmPassword = confirmPassword,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onResetPasswordClick = onResetPasswordClick,
                    onLoginClick = onLoginClick,
                    isLoading = isLoading,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
        }
    }
}

@Composable
fun EmailInputStep(
    email: String,
    onEmailValueChange: (String) -> Unit,
    onSendPasswordResetClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    isEmailError: Boolean,
    emailErrorMessage: String?
) {
    val focusManager = LocalFocusManager.current

    Text(
        text = stringResource(R.string.reset_password),
        style = TextStyle(
            fontSize = 22.sp,
            fontFamily = AlexandriaBlack,
            fontWeight = FontWeight.Black,
            lineHeight = 20.sp,
            color = DentaryBlue
        )
    )
    Spacer(modifier = Modifier.height(38.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DentaryLightGray,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(34.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.reset_password_message),
            modifier = Modifier.width(256.dp),
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = DentaryBlue
            ),
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.height(42.dp))
    CommonTextField(
        value = email,
        onValueChange = onEmailValueChange,
        label = stringResource(id = R.string.email),
        isError = isEmailError,
        errorMessage = emailErrorMessage,
        trailingIcon = painterResource(id = R.drawable.ic_email),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSendPasswordResetClick()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(42.dp))
    CommonButton(
        text = stringResource(R.string.send),
        onClick = onSendPasswordResetClick,
        isLoading = isLoading,
    )
    Spacer(modifier = Modifier.height(42.dp))
    BottomLoginSection(onLoginClick)
}

@Composable
fun OTPVerificationStep(
    email: String,
    otpCode: String,
    onOTPCodeChange: (String) -> Unit,
    onVerifyOTPClick: () -> Unit,
    onResendClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    isResending: Boolean,
    otpError: String,
    canResend: Boolean,
    resendCountdown: Int
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(
                color = DentaryBlue,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_email_white),
            contentDescription = null,
            modifier = Modifier.width(50.dp),
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Verify OTP",
        style = TextStyle(
            fontSize = 22.sp,
            fontFamily = AlexandriaBlack,
            fontWeight = FontWeight.Black,
            lineHeight = 20.sp,
            color = DentaryBlue
        )
    )

    if (email.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = email,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
                textAlign = TextAlign.Center,
            )
        )
    }

    Spacer(modifier = Modifier.height(38.dp))
    CommonTextField(
        value = otpCode,
        onValueChange = onOTPCodeChange,
        label = stringResource(R.string.verification_code),
        errorMessage = if (otpError.isNotEmpty()) otpError else null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(38.dp))
    Text(
        text = stringResource(R.string.did_not_receive_email),
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = AlexandriaRegular,
            fontWeight = FontWeight.Normal,
            color = DentaryBlue,
            textAlign = TextAlign.Center,
        )
    )
    TextButton(
        onClick = onResendClick,
        enabled = canResend && !isResending
    ) {
        if (isResending) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = DentaryBlue
            )
        } else if (!canResend) {
            val minutes = resendCountdown / 60
            val seconds = resendCountdown % 60
            val timeText = if (resendCountdown >= 60) {
                "${minutes}:${seconds.toString().padStart(2, '0')}"
            } else {
                "${seconds}s"
            }
            Text(
                text = "${stringResource(R.string.resend)} ($timeText)",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA2A2A2),
                    textAlign = TextAlign.Center,
                )
            )
        } else {
            Text(
                text = stringResource(R.string.resend),
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
    Spacer(modifier = Modifier.height(42.dp))
    CommonButton(
        text = "Verify OTP",
        onClick = onVerifyOTPClick,
        enabled = !isLoading && otpCode.isNotEmpty(),
        isLoading = isLoading
    )
    Spacer(modifier = Modifier.height(42.dp))
    BottomLoginSection(onLoginClick)
}

@Composable
fun PasswordChangeStep(
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean,
    passwordError: String?,
    confirmPasswordError: String?
) {
    val focusManager = LocalFocusManager.current

    Text(
        text = "Set New Password",
        style = TextStyle(
            fontSize = 22.sp,
            fontFamily = AlexandriaBlack,
            fontWeight = FontWeight.Black,
            lineHeight = 20.sp,
            color = DentaryBlue
        )
    )
    Spacer(modifier = Modifier.height(38.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DentaryLightGray,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(34.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Please enter your new password. Make sure it's secure and easy to remember.",
            modifier = Modifier.width(256.dp),
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                color = DentaryBlue
            ),
            textAlign = TextAlign.Center
        )
    }
    Spacer(modifier = Modifier.height(42.dp))

    CommonTextField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        label = "New Password",
        isError = passwordError != null,
        errorMessage = passwordError,
        trailingIcon = painterResource(id = R.drawable.ic_lock),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(24.dp))

    CommonTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = "Confirm Password",
        isError = confirmPasswordError != null,
        errorMessage = confirmPasswordError,
        trailingIcon = painterResource(id = R.drawable.ic_lock),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onResetPasswordClick()
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(42.dp))

    CommonButton(
        text = "Reset Password",
        onClick = onResetPasswordClick,
        enabled = !isLoading && newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
        isLoading = isLoading
    )
    Spacer(modifier = Modifier.height(42.dp))
    BottomLoginSection(onLoginClick)
}

@Composable
fun BottomLoginSection(onLoginClick: () -> Unit) {
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