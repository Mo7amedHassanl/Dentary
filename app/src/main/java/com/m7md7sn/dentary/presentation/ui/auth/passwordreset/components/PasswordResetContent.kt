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

@Composable
fun PasswordResetContent(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 26.dp, horizontal = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            value = "",
            onValueChange = {},
            label = stringResource(id = R.string.email),
            isError = false,
            errorMessage = null,
            trailingIcon = painterResource(id = R.drawable.ic_email),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
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
            onClick = {}
        )
    }
}