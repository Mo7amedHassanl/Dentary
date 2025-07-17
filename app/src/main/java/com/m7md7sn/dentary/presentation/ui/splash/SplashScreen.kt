package com.m7md7sn.dentary.presentation.ui.splash

import android.view.animation.OvershootInterpolator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.splash.components.AnimatedLogo
import com.m7md7sn.dentary.presentation.ui.splash.components.DentaryName
import com.m7md7sn.dentary.presentation.ui.splash.components.SplashContent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
    }

    // Handle navigation based on UI state
    LaunchedEffect(uiState.navigationState) {
        when (uiState.navigationState) {
            NavigationState.NavigateToHome -> {
                onNavigateToHome()
                viewModel.resetNavigationState()
            }
            NavigationState.NavigateToLogin -> {
                onNavigateToLogin()
                viewModel.resetNavigationState()
            }
            null -> {
                // Still loading or initial state
            }
        }
    }

    SplashContent(modifier, animationPhase, scale)
}
