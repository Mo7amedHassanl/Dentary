package com.m7md7sn.dentary.presentation.common.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue

@Composable
fun ImageSourcePickerDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.choose_image),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        text = {
            Text(
                text = stringResource(R.string.select_image_source),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onGalleryClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DentaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.gallery),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCameraClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DentaryBlue
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.camera),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        modifier = modifier,
        containerColor = BackgroundColor,
        titleContentColor = DentaryDarkBlue,
        textContentColor = DentaryBlue,
        icon = {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                tint = DentaryDarkBlue
            )
        }
    )
}
