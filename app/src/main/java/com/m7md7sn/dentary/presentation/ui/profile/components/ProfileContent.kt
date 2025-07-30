package com.m7md7sn.dentary.presentation.ui.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.m7md7sn.dentary.presentation.ui.profile.ProfileUiState

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEditProfileClick: () -> Unit,
    onSeeAllPatientsClick: () -> Unit,
    onUpdateProfilePicture: (android.net.Uri) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileTitle()
        ProfileUserInformation(
            uiState = uiState,
            onEditProfileClick = onEditProfileClick,
            onSeeAllPatientsClick = onSeeAllPatientsClick,
            onUpdateProfilePicture = onUpdateProfilePicture
        )
    }
}