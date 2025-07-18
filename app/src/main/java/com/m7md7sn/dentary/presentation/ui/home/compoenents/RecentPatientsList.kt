package com.m7md7sn.dentary.presentation.ui.home.compoenents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientList

@Composable
fun RecentPatientsList(
    modifier: Modifier = Modifier,
    patients: List<Patient> = emptyList(),
    onPatientSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RecentPatientsTitle(onPatientSeeAllClick = onPatientSeeAllClick)
        Spacer(Modifier.height(12.dp))
        PatientList(
            patients = patients,
        )
    }
}

@Composable
fun RecentPatientsTitle(
    onPatientSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_patient),
            contentDescription = stringResource(R.string.patients),
            modifier = Modifier.size(width =17.4.dp, height = 16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.recent_added_patients),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = AlexandriaBold,
                lineHeight = 18.sp,
                color = DentaryBlue
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onPatientSeeAllClick,
        ) {
            Text(
                text = stringResource(R.string.see_all),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = AlexandriaMedium,
                    lineHeight = 30.sp,
                    color = DentaryDarkGray
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}