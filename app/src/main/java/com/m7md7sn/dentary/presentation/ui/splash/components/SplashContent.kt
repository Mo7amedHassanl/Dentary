package com.m7md7sn.dentary.presentation.ui.splash.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

@Composable
fun SplashContent(
    modifier: Modifier,
    animationPhase: Int,
    scale: Animatable<Float, AnimationVector1D>
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = DentaryBlue),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = animationPhase,
            transitionSpec = {
                fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
            }
        ) { phase ->
            Column(
                modifier = Modifier.width(171.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedLogo(
                    scale = scale.value
                )
                if (phase == 1) {
                    DentaryName()
                }
            }
        }
    }
}
