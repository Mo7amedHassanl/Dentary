package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue

@Composable
fun AddPatientImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(134.dp)
                .padding(8.dp)
        )
        IconButton(
            onClick = { /* Handle image click */ },
            modifier = Modifier
                .size(40.dp)
                .background(DentaryLightBlue, CircleShape)
                .align(Alignment.BottomEnd)
                .padding(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                tint = Color.White
            )
        }
    }
}