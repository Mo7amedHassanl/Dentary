package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen

@Composable
fun AppSupport(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.help_support),
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
            )
        )
        Spacer(Modifier.height(16.dp))
        SettingsItem(
            text = stringResource(R.string.faq),
            onClick = {
                onNavigateToScreen(SettingsScreen.FAQ)
            }
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.call_support),
            onClick = {
                onNavigateToScreen(SettingsScreen.ContactSupport)
            }
        )
    }
}