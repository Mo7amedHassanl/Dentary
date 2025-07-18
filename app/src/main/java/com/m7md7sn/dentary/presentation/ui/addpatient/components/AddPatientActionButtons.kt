package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue

@Composable
fun AddPatientActionButtons(
    isLoading: Boolean = false,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AddPatientActionButton(
            text = stringResource(R.string.save_patient),
            hasBorder = false,
            onClick = onSaveClick,
            enabled = !isLoading,
        )

        AddPatientActionButton(
            text = stringResource(R.string.cancel),
            hasBorder = true,
            onClick = onCancelClick,
            backgroundColor = Color.White,
            enabled = !isLoading,
        )
    }
}

@Composable
fun AddPatientActionButton(
    text: String,
    hasBorder: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DentaryDarkBlue,
    fontSize: TextUnit = 15.sp,
    fontFamily: FontFamily = AlexandriaBold,
    fontWeight: FontWeight = FontWeight.Bold,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(width = 148.dp, height = 58.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(
                width = if (!hasBorder) 0.dp else 1.dp,
                color = if (!hasBorder) Color.Transparent else DentaryDarkBlue,
                shape = CircleShape
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            color = if (hasBorder) DentaryDarkBlue else Color.White,
            lineHeight = fontSize
        )
    }
}