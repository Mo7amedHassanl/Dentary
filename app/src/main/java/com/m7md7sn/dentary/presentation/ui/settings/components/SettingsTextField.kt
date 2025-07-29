package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray


@Preview
@Composable
fun SettingsTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "البريد الإلكتروني",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    icon: Int = R.drawable.ic_email,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = DentaryBlue,
            focusedTextColor = DentaryBlue,
            unfocusedTextColor = DentaryBlueGray,
            disabledTextColor = DentaryBlueGray,
            disabledContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledBorderColor = DentaryBlueGray,
            focusedBorderColor = DentaryBlue,
            unfocusedBorderColor = DentaryBlueGray,
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isError) Color.Red else DentaryBlueGray,
                    fontFamily = AlexandriaRegular
                ),
                textAlign = TextAlign.Center
            )
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                )
            }
        } else null,
        visualTransformation = visualTransformation,
        enabled = enabled,
        shape = CircleShape,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = if (isError) Color.Red else DentaryBlue,
            fontFamily = AlexandriaRegular
        ),
        leadingIcon = {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
            )
        }
    )
}

@Preview
@Composable
fun SettingsTextFieldNoIcon(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "البريد الإلكتروني",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = DentaryBlue,
            focusedTextColor = DentaryBlue,
            unfocusedTextColor = DentaryBlueGray,
            disabledTextColor = DentaryBlueGray,
            disabledContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledBorderColor = DentaryBlueGray,
            focusedBorderColor = DentaryBlue,
            unfocusedBorderColor = DentaryBlueGray,
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isError) Color.Red else DentaryBlueGray,
                    fontFamily = AlexandriaRegular
                ),
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    style = TextStyle(
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                )
            }
        } else null,
        visualTransformation = visualTransformation,
        enabled = enabled,
        shape = CircleShape,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = if (isError) Color.Red else DentaryBlue,
            fontFamily = AlexandriaRegular
        ),
    )
}