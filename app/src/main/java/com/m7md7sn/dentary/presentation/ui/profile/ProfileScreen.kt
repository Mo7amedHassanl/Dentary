package com.m7md7sn.dentary.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.profile.components.ProfileContent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    // Handle snackbar messages
    LaunchedEffect(viewModel) {
        viewModel.snackbarMessage.collect { event ->
            event.getContentIfNotHandled()?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

    Surface(
        color = BackgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = true)
                    .padding(vertical = 8.dp, horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProfileContent(
                    uiState = uiState,
                    onEditProfileClick = viewModel::onEditProfileClick,
                    onSeeAllPatientsClick = viewModel::onSeeAllPatientsClick,
                    onRefresh = viewModel::refreshProfile
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

    }
}

@Preview
@Composable
private fun ProfileScreenPreviewEn() {
    DentaryTheme {
        ProfileScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun ProfileScreenPreviewAr() {
    DentaryTheme {
        ProfileScreen()
    }
}