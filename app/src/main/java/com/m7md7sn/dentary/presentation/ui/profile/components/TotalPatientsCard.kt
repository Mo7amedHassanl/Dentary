package com.m7md7sn.dentary.presentation.ui.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.AlexandriaSemiBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.data.model.MedicalProcedureStats


@Composable
fun TotalPatientsCard(
    totalPatients: Int,
    medicalProcedureStats: List<MedicalProcedureStats>,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DentaryLightBlue
        ),
        onClick = {},
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 3.dp)
                .wrapContentHeight()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_patient),
                    modifier = Modifier.size(width = 22.dp, height = 24.dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.total_patients),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        color = DentaryDarkBlue
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
                TextButton(
                    onClick = onSeeAllClick,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = totalPatients.toString(),
                    style = TextStyle(
                        fontSize = 42.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = DentaryDarkBlue
                    )
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.patient),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp,
                        color = DentaryLightBlue
                    )
                )
            }
            Spacer(Modifier.height(20.dp))
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(medicalProcedureStats) { stat ->
                    Row(
                        modifier = Modifier
                            .width(146.dp)
                            .height(26.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFE9EDF8), CircleShape),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.6f)
                                .background(Color(0xFFE9EDF8), shape = CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stat.procedure,
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontFamily = AlexandriaSemiBold,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DentaryDarkBlue,
                                )
                            )
                        }
                        Text(
                            text = stat.count.toString(),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = AlexandriaBold,
                                fontWeight = FontWeight.Bold,
                                color = DentaryBlue,
                            ),
                            modifier = Modifier.weight(0.4f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}