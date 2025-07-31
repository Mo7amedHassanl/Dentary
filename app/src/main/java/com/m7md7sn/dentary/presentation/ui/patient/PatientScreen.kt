package com.m7md7sn.dentary.presentation.ui.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.patient.components.PatientHeader
import com.m7md7sn.dentary.presentation.ui.profile.components.PatientContent
import com.m7md7sn.dentary.presentation.ui.profile.components.ProfileContent

@Composable
fun PatientScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Surface(
        color = DentaryLightBlue,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = DentaryLightBlue)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.patient_header_bg),
                        contentDescription = null
                    )
                    PatientHeader()
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(color = BackgroundColor, shape = RoundedCornerShape(topEnd = 35.dp, topStart = 35.dp)),
                    contentAlignment = Alignment.TopCenter
                ) {
                    PatientContent()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PatientScreenPreviewEn() {
    DentaryTheme {
        PatientScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun PatientScreenPreviewAr() {
    DentaryTheme {
        PatientScreen()
    }
}