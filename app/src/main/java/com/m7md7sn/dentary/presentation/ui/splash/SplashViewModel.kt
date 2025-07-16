package com.m7md7sn.dentary.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> =
        _uiState.asStateFlow()

    suspend fun isUserSignedIn(): Boolean {
        var isSignedIn = false
        viewModelScope.launch {
            isSignedIn = authRepository.getCurrentUser() != null
            _uiState.update { it.copy(isUserSignedIn = isSignedIn) }
        }
        return isSignedIn
    }

    suspend fun checkAuthAndConnectivityState(): NavigationState {
        val isSignedIn = isUserSignedIn()

        return when {
            isSignedIn -> NavigationState.NavigateToHome
            else -> NavigationState.NavigateToLogin
        }
    }

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isUserSignedIn = isUserSignedIn(),
                )
            }
        }
    }
}