package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryGray
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightGray

@Composable
fun SettingsItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable(true){
                onClick()
            },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = AlexandriaMedium,
                    fontWeight = FontWeight.Medium,
                    color = DentaryDarkBlue,
                ),
            )
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.height(18.dp).width(12.dp),
                    tint = Color(0xFFD8DEEF)
                )
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = AlexandriaMedium,
                    fontWeight = FontWeight.Medium,
                    color = DentaryDarkBlue,
                ),
            )
            Switch(
                checked = true,
                onCheckedChange = {},
                colors = androidx.compose.material3.SwitchDefaults.colors(
                    checkedThumbColor = DentaryLightGray,
                    uncheckedThumbColor = DentaryDarkBlue,
                    checkedTrackColor = DentaryLightBlue,
                    uncheckedTrackColor = DentaryLightGray.copy(alpha = 0.5f),
                    uncheckedBorderColor = DentaryLightGray
                ),
                modifier = Modifier.height(20.dp),
            )
        }
    }
}