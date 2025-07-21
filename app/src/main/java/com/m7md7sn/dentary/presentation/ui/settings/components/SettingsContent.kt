package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.ui.profile.components.ProfileTitle
import com.m7md7sn.dentary.presentation.ui.profile.components.ProfileUserInformation

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingsTitle()
        Spacer(Modifier.height(22.dp))
        AccountSettings()
        Spacer(Modifier.height(18.dp))
        NotificationsSettings()
        Spacer(Modifier.height(18.dp))
        AppSettings()
        Spacer(Modifier.height(18.dp))
        AppSupport()
    }
}