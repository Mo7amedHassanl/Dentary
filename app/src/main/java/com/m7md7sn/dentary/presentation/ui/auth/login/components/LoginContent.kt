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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.common.components.CommonButton
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

@Composable
fun LoginContent(
    onCreateNewAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        LoginTextFields(
            modifier = Modifier.fillMaxWidth(),
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            emailError = "",
            passwordError = "",
            isEmailError = false,
            isPasswordError = false,
        )
        ForgetPasswordButton(
            onClick = {}
        )
        Spacer(Modifier.height(8.dp))
        CommonButton(
            text = stringResource(R.string.join),
            onClick = {}
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
            onClick = {
                onCreateNewAccountClick()
            },
        ) {
            Text(
                text = stringResource(R.string.create_new_account),
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

@Preview
@Composable
private fun LoginContentPrev() {
    LoginContent({})
}

