package com.m7md7sn.dentary.presentation.ui.auth.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.common.components.CommonButton
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.splash.components.DentaryName
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(300L)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
                    .background(
                        color = DentaryBlue.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_white),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(DentaryBlue)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            DentaryName(
                dentaryFontSize = 32.sp,
                sloganFontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Welcome to Dentary!",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontFamily = AlexandriaBlack,
                    fontWeight = FontWeight.Black,
                    color = DentaryBlue
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your clinical practice is now more organized and efficient than ever. We're happy to have you join our community of dentists.",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    color = Color(0xFFA2A2A2)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.weight(1.2f))

            CommonButton(
                text = "Get Started",
                onClick = onGetStartedClick,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    DentaryTheme {
        WelcomeScreen(onGetStartedClick = {})
    }
}
