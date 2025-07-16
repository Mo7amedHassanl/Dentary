package com.m7md7sn.dentary.presentation.ui.splash

data class SplashUiState(
    val isLoading: Boolean = true,
    val isUserSignedIn: Boolean = false,
)

sealed class NavigationState {
    object NavigateToHome : NavigationState()
    object NavigateToLogin : NavigationState()
}
