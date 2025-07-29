package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit,
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsTitle()
        Spacer(Modifier.height(22.dp))
        AccountSettings(onNavigateToScreen = onNavigateToScreen)
        Spacer(Modifier.height(18.dp))
        NotificationsSettings(onNavigateToScreen = onNavigateToScreen)
        Spacer(Modifier.height(18.dp))
        AppSettings(onNavigateToScreen = onNavigateToScreen)
        Spacer(Modifier.height(18.dp))
        AppSupport(onNavigateToScreen = onNavigateToScreen)
        Spacer(Modifier.height(18.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(R.string.logout),
                style = TextStyle(
                    fontSize = 17.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                )
            )
            Spacer(Modifier.height(16.dp))
            SettingsItem(
                text = stringResource(R.string.logout),
                onClick = onLogout
            )
        }
    }
}