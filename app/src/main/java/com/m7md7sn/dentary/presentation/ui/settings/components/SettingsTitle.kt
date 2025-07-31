package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

@Composable
fun SettingsTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = stringResource(R.string.settings),
            modifier = Modifier.size(width = 24.dp, height = 26.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.settings),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = AlexandriaBold,
                lineHeight = 24.sp,
                color = DentaryBlue
            ),
            textAlign = TextAlign.Center
        )
    }
}