package com.m7md7sn.dentary.presentation.ui.medicalhistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.ui.settings.components.SettingsActionButton

@Composable
fun AddMedicalHealthActionButtons(
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    isSaving: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterHorizontally),
    ) {
        SettingsActionButton(
            text = if (isSaving) stringResource(R.string.saving) else stringResource(R.string.save),
            hasBorder = false,
            onClick = onSave,
            enabled = !isSaving,
        )

        SettingsActionButton(
            text = stringResource(R.string.cancel),
            hasBorder = true,
            onClick = onCancel,
            backgroundColor = Color.White,
            enabled = !isSaving,
        )
    }
}
