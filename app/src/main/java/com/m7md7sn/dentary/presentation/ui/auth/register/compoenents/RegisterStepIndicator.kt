package com.m7md7sn.dentary.presentation.ui.auth.register.compoenents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.ui.auth.register.RegisterStep

@Composable
fun RegisterStepIndicator(
    currentStep: RegisterStep,
    modifier: Modifier = Modifier
) {
    val steps = RegisterStep.entries

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isActive = step == currentStep
            val isCompleted = steps.indexOf(currentStep) > index

            RegisterStepItem(
                index = index + 1,
                isActive = isActive,
                isCompleted = isCompleted
            )

            if (index < steps.size - 1) {
                Spacer(
                    modifier = Modifier
                        .size(width = 60.dp, height = 2.dp)
                        .background(if (isCompleted) DentaryBlue else DentaryBlueGray)
                )
            }
        }
    }
}

@Composable
private fun RegisterStepItem(
    index: Int,
    isActive: Boolean,
    isCompleted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (isActive || isCompleted) DentaryBlue else DentaryBlueGray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(
                    text = index.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
