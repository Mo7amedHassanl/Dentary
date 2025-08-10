package com.m7md7sn.dentary.presentation.ui.patient.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue

data class DetailsListItem(
    @DrawableRes val icon: Int,
    @StringRes val text: Int,
    val onClick: () -> Unit
)

val patientDetailsListItems = listOf(
    DetailsListItem(
        icon = R.drawable.ic_medical_history,
        text = R.string.medical_history,
        onClick = {}
    ),
    DetailsListItem(
        icon = R.drawable.ic_treatment_plan,
        text = R.string.treatment_plan,
        onClick = {}
    ),
    DetailsListItem(
        icon = R.drawable.ic_tooth_structure,
        text = R.string.teeth_structure,
        onClick = {}
    ),
    DetailsListItem(
        icon = R.drawable.ic_medical_note,
        text = R.string.medical_note,
        onClick = {}
    ),
    DetailsListItem(
        icon = R.drawable.ic_patient_pictures,
        text = R.string.patient_pictures,
        onClick = {}
    ),
)

@Composable
fun PatientDetailsList(
    items: List<DetailsListItem> = patientDetailsListItems,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(){
            items(items){ item ->
                PatientDetailsListItem(
                    icon = item.icon,
                    text = item.text,
                    onClick = item.onClick,
                )
            }
        }
    }
}

@Composable
fun PatientDetailsListItem(
    @DrawableRes icon: Int = R.drawable.ic_medical_history,
    text: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(bottom = 6.dp)
            .width(200.dp)
            .height(40.dp),
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 10.dp, horizontal = 16.dp
                )
        ){
            Image(
                painter = painterResource(id = icon),
                contentDescription = stringResource(id = text),
                modifier = Modifier.align(Alignment.CenterStart)
            )
            Text(
                text = stringResource(id = text),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = DentaryLighterBlue,
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}