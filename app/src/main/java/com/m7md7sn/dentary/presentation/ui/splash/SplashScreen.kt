package com.m7md7sn.dentary.presentation.ui.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.splash.components.AnimatedLogo
import com.m7md7sn.dentary.presentation.ui.splash.components.DentaryName
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale = remember {
        Animatable(0f)
    }
    var animationPhase by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator().getInterpolation(it)
                }
            )
        )
        animationPhase = 1
        delay(800)
        onNavigateToHome()
    }

    SplashContent(modifier, animationPhase, scale)
}

@Composable
private fun SplashContent(
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

@Preview
@Composable
private fun SplashScreenPreviewEn() {
    DentaryTheme {
        SplashScreen(
            onNavigateToHome = {}
        )
    }
}

@Preview(locale = "ar")
@Composable
private fun SplashScreenPreviewAr() {
    DentaryTheme {
        SplashScreen(
            onNavigateToHome = {}
        )
    }
}