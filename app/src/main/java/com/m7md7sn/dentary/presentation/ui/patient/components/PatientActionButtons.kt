package com.m7md7sn.dentary.presentation.ui.patient.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue

data class PatientActionButtonData(
    val onClick: () -> Unit,
    val buttonColor: Color,
    val iconColor: Color,
    @DrawableRes val icon: Int
)

val patientActionButtonList = listOf(
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color(0xFF073FB8),
        iconColor = Color.White,
        icon = R.drawable.ic_edit
    ),
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color.White,
        iconColor = DentaryLightBlue,
        icon = R.drawable.ic_call
    ),
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color(0xFF5F67EC),
        iconColor = Color.White,
        icon = R.drawable.ic_chat
    ),
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color(0xFF6FE074),
        iconColor = Color.White,
        icon = R.drawable.ic_whatsapp
    ),
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color(0xFFEF6F5E),
        iconColor = Color.White,
        icon = R.drawable.ic_pdf
    ),
    PatientActionButtonData(
        onClick = { },
        buttonColor = Color(0xFF33CCCC),
        iconColor = Color.White,
        icon = R.drawable.ic_appoitment
    )
)

@Preview
@Composable
fun PatientActionButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        patientActionButtonList.forEach { patientActionButtonData ->
            PatientActionButton(
                onClick = patientActionButtonData.onClick,
                buttonColor = patientActionButtonData.buttonColor,
                iconColor = patientActionButtonData.iconColor,
                icon = patientActionButtonData.icon,
            )
        }
    }
}

@Composable
fun PatientActionButton(
    onClick: () -> Unit,
    buttonColor: Color,
    iconColor: Color,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .background(buttonColor, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
        ,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor,
            contentColor = iconColor
        )
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}