package com.m7md7sn.dentary.presentation.ui.splash.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.MontserratBlack
import com.m7md7sn.dentary.presentation.theme.MontserratLight

@Composable
fun DentaryName(
    modifier: Modifier = Modifier,
    dentaryFontSize: TextUnit = 40.sp,
    sloganFontSize: TextUnit = 17.sp
) {
    val appName = stringResource(id = R.string.app_name)
    val startIndex = 0
    val endIndex = appName.indexOf('t')

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = MontserratBlack,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append(appName.substring(startIndex, endIndex))
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = MontserratLight,
                        fontWeight = FontWeight.Light,
                    )
                ) {
                    append(appName.substring(endIndex))
                }
            },
            style = TextStyle(
                fontSize = dentaryFontSize,
                color = Color.White,
                lineHeight = 48.sp,
                textAlign = TextAlign.Center
            ),
        )
        Text(
            text = stringResource(id = R.string.app_slogan),
            style = TextStyle(
                fontSize = sloganFontSize,
                color = Color(0xFF9EABB7),
                fontFamily = MontserratLight,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            ),
        )
    }
}

@Preview
@Composable
private fun DentaryName() {

}