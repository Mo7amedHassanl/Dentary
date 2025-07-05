package com.m7md7sn.dentary.presentation.ui.splash.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentary.R

@Composable
fun AnimatedLogo(
    scale: Float,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.logo_white),
        contentDescription = stringResource(id = R.string.logo_description),
        modifier = modifier.scale(scale)
    )
}

@Preview
@Composable
private fun AnimatedLogoPrev() {
    AnimatedLogo(
        scale = 1f
    )
}