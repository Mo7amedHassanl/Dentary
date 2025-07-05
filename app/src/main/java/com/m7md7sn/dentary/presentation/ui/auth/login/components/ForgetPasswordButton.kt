package com.m7md7sn.dentary.presentation.ui.auth.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular

@Composable
fun ForgetPasswordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.forget_password),
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = AlexandriaRegular,
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA2A2A2),
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
