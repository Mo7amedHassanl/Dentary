package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsItem
import com.m7md7sn.dentary.presentation.ui.settings.components.common.SettingsTextField

@Composable
fun AppSupport(
    modifier: Modifier = Modifier,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.help_support),
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = AlexandriaBold,
                fontWeight = FontWeight.Bold,
                color = DentaryBlue,
            )
        )
        Spacer(Modifier.height(16.dp))
        SettingsItem(
            text = stringResource(R.string.faq),
            onClick = {
                onNavigateToScreen(SettingsScreen.FAQ)
            }
        )
        Spacer(Modifier.height(8.dp))
        SettingsItem(
            text = stringResource(R.string.call_support),
            onClick = {
                onNavigateToScreen(SettingsScreen.ContactSupport)
            }
        )
    }
}

@Composable
fun SupportContent(
    email: String,
    message: String,
    isEmailError: Boolean,
    isMessageError: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        SectionTitle(
            title = R.string.call_support,
            titleIcon = R.drawable.ic_phone,
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        SettingsTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = stringResource(R.string.email),
            icon = R.drawable.ic_user,
            isError = isEmailError,
            errorMessage = if (isEmailError) "Invalid email format" else null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message field
        SettingsTextField(
            value = message,
            onValueChange = onMessageChange,
            placeholder = stringResource(R.string.inquiry),
            isError = isMessageError,
            errorMessage = if (isMessageError) "Message cannot be empty" else null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(24.dp),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            icon = R.drawable.ic_email
        )

        Spacer(modifier = Modifier.height(24.dp))
        SettingsActionButtons(
            onSaveClick = {  },
            onCancelClick = {  }
        )
    }
}