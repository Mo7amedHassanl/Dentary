package com.m7md7sn.dentary.presentation.ui.splash

data class SplashUiState(
    val isLoading: Boolean = false,
    val isUserSignedIn: Boolean = false,
    val navigationState: NavigationState = NavigationState.Idle
)

sealed class NavigationState {
    object Idle : NavigationState()
    object NavigateToHome : NavigationState()
    object NavigateToLogin : NavigationState()
}