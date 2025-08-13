package com.m7md7sn.dentary.presentation.ui.medicalhistory.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsTextFieldNoIcon

@Composable
fun AddMedicalHistoryContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp, horizontal = 29.dp)
    ) {
        Subtitle(
            icon = R.drawable.ic_description,
            text = R.string.description
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingsTextFieldNoIcon(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(20.dp),
            placeholder = stringResource(R.string.enter_description),
        )
        Spacer(modifier = Modifier.height(22.dp))
        Subtitle(
            icon = R.drawable.ic_attachments,
            text = R.string.attachments
        )
        Spacer(modifier = Modifier.height(16.dp))
        MedicalHistoryAttachmentsList(
            attachments = listOf("","") // Replace with actual attachments data
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddMedicalHealthActionButtons()
    }
}

@Composable
private fun Subtitle(
    icon: Int = R.drawable.ic_description,
    text: Int = R.string.description
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 21.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(text),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(text),
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue
            )
        )
    }
}