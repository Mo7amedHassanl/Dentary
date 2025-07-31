package com.m7md7sn.dentary.presentation.ui.auth.emailverification.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun EmailVerificationContent(
    email: String = "",
    otpCode: String = "",
    otpError: String = "",
    onOTPCodeChange: (String) -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onResendClick: () -> Unit = {},
    isLoading: Boolean = false,
    isResending: Boolean = false,
    canResend: Boolean = true,
    resendCountdown: Int = 0,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 26.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            text = stringResource(R.string.confirm_your_email),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = AlexandriaBlack,
                fontWeight = FontWeight.Black,
                lineHeight = 20.sp,
                color = DentaryBlue
            )
        )

        // Add email display
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
                    String.format("%d:%02d", minutes, seconds)
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
            text = stringResource(R.string.confirm_email),
            onClick = {
                println("EmailVerificationContent: Confirm button clicked!")
                onConfirmClick()
            },
            enabled = !isLoading && otpCode.isNotEmpty(),
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(42.dp))
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
            onClick = {},
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