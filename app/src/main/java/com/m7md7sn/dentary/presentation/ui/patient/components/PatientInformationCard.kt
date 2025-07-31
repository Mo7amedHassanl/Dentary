package com.m7md7sn.dentary.presentation.ui.patient.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaExtraBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.with

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PatientInformationCard(
    patient: Patient,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = isExpanded,
        transitionSpec = {
            if (targetState) {
                expandVertically(expandFrom = Alignment.Top) with shrinkVertically(shrinkTowards = Alignment.Top)
            } else {
                expandVertically(expandFrom = Alignment.Top) with shrinkVertically(shrinkTowards = Alignment.Top)
            }
        }
    ) { expanded ->
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(if (!expanded) 42.dp else 250.dp),
            shape = RoundedCornerShape(21.dp),
            onClick = { isExpanded = !isExpanded },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFFE3E7F2)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = stringResource(
                            R.string.personal_info
                        ),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = AlexandriaExtraBold,
                            fontWeight = FontWeight.ExtraBold,
                            color = DentaryDarkBlue,
                        )
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = DentaryBlueGray,
                        modifier = Modifier
                            .height(12.dp)
                            .rotate(
                                if (expanded) 90f else 0f
                            )
                    )
                }

                if (expanded) {
                    ExpandedPatientInformation(patient = patient)

                }
            }
        }
    }
}

@Composable
fun ExpandedPatientInformation(
    patient: Patient,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .height(54.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.age) + ":",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF697988),
                    )
                )
                Text(
                    text = patient.age?.toString() ?: "-",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = DentaryDarkBlue,
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DentaryLighterBlue
                )
            }
            Column(
                modifier = modifier
                    .weight(1f)
                    .height(54.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.gender) + ":",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF697988),
                    )
                )
                Text(
                    text = patient.gender ?: "-",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = DentaryDarkBlue,
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF6A56CC)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .height(54.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.email) + ":",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF697988),
                    )
                )
                Text(
                    text = patient.email ?: "-",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = DentaryDarkBlue,
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = DentaryLighterBlue
                )
            }
            Column(
                modifier = modifier
                    .weight(1f)
                    .height(54.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.phone_number) + ":",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF697988),
                    )
                )
                Text(
                    text = patient.phoneNumber ?: "-",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = DentaryDarkBlue,
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF6A56CC)
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(54.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.address) + ":",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF697988),
                )
            )
            Text(
                text = patient.address ?: "-",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                    color = DentaryDarkBlue,
                )
            )
        }
    }
}