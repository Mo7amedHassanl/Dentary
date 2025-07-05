package com.m7md7sn.dentary.presentation.ui.auth.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.ui.splash.components.DentaryName

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp
                    )
                ),
        ) {
            Image(
                painter = painterResource(R.drawable.img_doctor),
                contentDescription = stringResource(R.string.login_header_description),
                contentScale = ContentScale.FillHeight
            )
            DentaryLogoWithName(
                modifier = Modifier
                    .padding(end = 52.dp, top = 34.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }

}

@Composable
fun DentaryLogoWithName(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_white),
                contentDescription = stringResource(id = R.string.logo_description),
                modifier = Modifier.size(height = 66.dp, width = 58.dp),
            )
            DentaryName(
                dentaryFontSize = 27.sp,
                sloganFontSize = 10.sp
            )
        }
    }
}

@Preview()
@Composable
private fun LoginHeaderPrev() {
    LoginHeader()
}