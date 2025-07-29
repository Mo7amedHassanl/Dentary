package com.m7md7sn.dentary.presentation.ui.profile.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

@Composable
fun ProfileTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.profile),
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily =AlexandriaBold,
            fontWeight = FontWeight.Bold,
            color = DentaryBlue,
        )
    )
}