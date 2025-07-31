package com.m7md7sn.dentary.presentation.ui.home.compoenents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue

@Composable
fun HomeAppointments(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppointmentsTitle()
        Spacer(Modifier.width(12.dp))
        AppointmentCards()
    }
}

@Composable
fun AppointmentCards(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppointmentCard(
            title = R.string.today,
            number = 5,
            icon = R.drawable.ic_today_appointments,
            color = DentaryLightBlue,
        )
        AppointmentCard(
            title = R.string.upcoming,
            number = 10,
            icon = R.drawable.ic_upcoming_appointments,
            color = Color(0xFF5F67EC),
        )
    }
}

@Composable
fun AppointmentCard(
    @StringRes title: Int,
    number: Int,
    @DrawableRes icon: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.height(100.dp).width(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        onClick = {},
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 3.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(20.dp)),
        ) {
            Text(
                text = "$number",
                style = TextStyle(
                    fontSize = 42.sp,
                    fontFamily = AlexandriaBold,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 105.sp,
                    color = DentaryBlue
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(id = title),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = stringResource(id = title),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        color = DentaryDarkBlue
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AppointmentsTitle(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = stringResource(R.string.patients),
            modifier = Modifier.size(width =17.4.dp, height = 16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.appointments),
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
            onClick = {},
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