package com.m7md7sn.dentary.presentation.ui.medicalhistory.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsTextFieldNoIcon

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddAttachmentBottomSheet(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 16.dp),
        sheetState = sheetState,
        shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp,
    ) {
        SettingsTextFieldNoIcon(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            placeholder = stringResource(R.string.attachment_description),
        )
    }
}