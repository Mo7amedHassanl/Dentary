package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.utils.rememberImagePicker

@Composable
fun AddPatientImage(
    patientImageUrl: String?,
    onUpdatePatientImage: (android.net.Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePicker = rememberImagePicker() { uri ->
        uri?.let { onUpdatePatientImage(it) }
    }
    
    Box(
        modifier = modifier,
    ) {
        if (patientImageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(patientImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(134.dp)
                    .padding(8.dp)
                    .clip(CircleShape),

            )
        } else {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(134.dp)
                    .padding(8.dp)
                    .clickable(enabled = true, onClick = { imagePicker() })
            )
        }
        
        IconButton(
            onClick = { imagePicker() },
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