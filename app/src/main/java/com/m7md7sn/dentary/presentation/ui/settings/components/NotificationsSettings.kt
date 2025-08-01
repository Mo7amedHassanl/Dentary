package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsItem
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsRadioButton
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsSwitchItem

@Composable
fun NotificationsSettings(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.notifications_settings),
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
            )
        )
        Spacer(Modifier.height(16.dp))
        SettingsSwitchItem(
            text = stringResource(R.string.enable_reminders),
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.ringnotes_customization),
            onClick = {
                onNavigateToScreen(SettingsScreen.RingtoneCustomization)
            }
        )
    }
}

@Preview
@Composable
fun RingtoneCustomizationSettings(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        SectionTitle(
            title = R.string.ringnotes_customization,
            titleIcon = R.drawable.ic_bill,
        )
        Spacer(Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SettingsRadioButton()
            SettingsRadioButton()
            SettingsRadioButton()
        }
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = { /* Handle save action */ },
            onCancelClick = { /* Handle cancel action */ }
        )
    }
}