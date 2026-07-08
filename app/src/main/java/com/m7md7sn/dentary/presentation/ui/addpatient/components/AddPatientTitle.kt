package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue

@Composable
fun AddPatientTitle(
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false
) {
    val title = if (isEditMode) stringResource(R.string.edit_patient) else stringResource(R.string.add_patient)
    val icon = if (isEditMode) R.drawable.ic_edit else R.drawable.ic_add_patient

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(width = 27.5.dp, height = 25.5.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = AlexandriaBold,
                lineHeight = 24.sp,
                color = DentaryLighterBlue
            ),
            textAlign = TextAlign.Center
        )
    }
}