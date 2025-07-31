package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
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
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.logout_confirmation_title),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        text = {
            Text(
                text = stringResource(R.string.logout_confirmation_message),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Normal,
                )
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.logout),
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
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DentaryBlue
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
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
                imageVector = Icons.Filled.Logout,
                contentDescription = stringResource(R.string.logout_confirmation_title),
                tint = DentaryDarkBlue
            )
        }
    )
}