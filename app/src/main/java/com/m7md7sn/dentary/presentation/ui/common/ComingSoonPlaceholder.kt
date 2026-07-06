package com.m7md7sn.dentary.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue

@Composable
fun ComingSoonPlaceholder(
    modifier: Modifier = Modifier,
    iconRes: Int,
    titleRes: Int = R.string.coming_soon,
    descriptionRes: Int,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(DentaryBlue)
        )
        
        Spacer(Modifier.height(32.dp))
        
        Text(
            text = stringResource(id = titleRes),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryDarkBlue
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(12.dp))
        
        Text(
            text = stringResource(id = descriptionRes),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = AlexandriaRegular,
                fontWeight = FontWeight.Normal,
                color = DentaryBlue
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.feature_not_available),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = AlexandriaRegular,
                color = Color(0xFF697988)
            ),
            textAlign = TextAlign.Center
        )
    }
}
