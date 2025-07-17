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
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue

@Composable
fun AddPatientTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_add_patient),
            contentDescription = stringResource(R.string.add_patient),
            modifier = Modifier.size(width = 27.5.dp, height = 25.5.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.add_patient),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = AlexandriaBold,
                lineHeight = 24.sp,
                color = DentaryLightBlue
            ),
            textAlign = TextAlign.Center
        )
    }
}