package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.utils.rememberImagePickerWithCamera
import com.m7md7sn.dentary.presentation.common.components.ImageSourcePickerDialog

@Composable
fun AddPatientImage(
    patientImageUrl: String?,
    isImageUploading: Boolean,
    onUpdatePatientImage: (android.net.Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val controller = rememberImagePickerWithCamera { uri ->
        uri?.let { onUpdatePatientImage(it) }
    }
    
    var showPickerDialog by remember { mutableStateOf(false) }

    if (showPickerDialog) {
        ImageSourcePickerDialog(
            onGalleryClick = {
                showPickerDialog = false
                controller.pickFromGallery()
            },
            onCameraClick = {
                showPickerDialog = false
                controller.takePhoto()
            },
            onDismiss = { showPickerDialog = false }
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(134.dp)
                .clip(CircleShape)
                .background(Color(0xFFEEF1FC))
                .clickable(enabled = !isImageUploading, onClick = { showPickerDialog = true }),
            contentAlignment = Alignment.Center
        ) {
            if (patientImageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(patientImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isImageUploading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        
        if (!isImageUploading) {
            IconButton(
                onClick = { showPickerDialog = true },
                modifier = Modifier
                    .size(40.dp)
                    .background(DentaryLighterBlue, CircleShape)
                    .align(Alignment.BottomEnd)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
