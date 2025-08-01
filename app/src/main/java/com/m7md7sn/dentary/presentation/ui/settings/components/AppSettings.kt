package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
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
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsItem
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsRadioButton

@Composable
fun AppSettings(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.app_settings),
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
            )
        )
        Spacer(Modifier.height(16.dp))
        SettingsItem(
            text = stringResource(R.string.language),
            onClick = {
                onNavigateToScreen(SettingsScreen.Language)
            }
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.data_back_up),
            onClick = {
                onNavigateToScreen(SettingsScreen.DataBackup)
            }
        )
    }
}

@Composable
fun AppLanguageSettings(
    modifier: Modifier = Modifier,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onSaveClicked: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    val languageOptions = listOf("Arabic", "English")
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        SectionTitle(
            title = R.string.language,
            titleIcon = R.drawable.ic_location,
        )
        Spacer(Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            languageOptions.forEach {
                SettingsRadioButton(
                    selected = selectedLanguage == it,
                    onClick = { onLanguageSelected(it) },
                    text = it,
                )
            }
        }
        Spacer(Modifier.height(22.dp))
        SettingsActionButtons(
            onSaveClick = { onSaveClicked() },
            onCancelClick = { onBackClick() }
        )
    }
}