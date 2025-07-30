package com.m7md7sn.dentary.presentation.ui.home.compoenents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaExtraBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray

@Composable
fun HomeHeader(
    name: String,
    clinicName: String,
    profilePictureUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.welcome) + name,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = AlexandriaMedium,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 30.sp,
                    color = DentaryDarkGray
                ),
                textAlign = TextAlign.Start
            )
            Text(
                text = clinicName,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlexandriaExtraBold,
                    lineHeight = 30.sp,
                    color = Color(0xFF03277A)
                ),
                textAlign = TextAlign.Start
            )
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(DentaryBlue, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profilePictureUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .background(DentaryBlue, shape = CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_user_sharp),
                    contentDescription = null,
                )
            }
        }
    }
}