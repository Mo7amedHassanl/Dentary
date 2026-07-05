package com.m7md7sn.dentary.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkAuthenticationState()
    }

    private fun checkAuthenticationState() {
        if (_uiState.value.isLoading) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(1000) // Slightly longer splash for better UX

            try {
                val user = authRepository.getCurrentUser()
                val isValid = authRepository.isSessionValid()

                val navigationState = if (isValid && user != null) {
                    _uiState.update {
                        it.copy(
                            isUserSignedIn = true,
                            isLoading = false
                        )
                    }
                    NavigationState.NavigateToHome
                } else {
                    _uiState.update {
                        it.copy(
                            isUserSignedIn = false,
                            isLoading = false
                        )
                    }
                    NavigationState.NavigateToLogin
                }

                _uiState.update { it.copy(navigationState = navigationState) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUserSignedIn = false,
                        isLoading = false,
                        navigationState = NavigationState.NavigateToLogin
                    )
                }
            }
        }
    }

    fun resetNavigationState() {
        _uiState.update { it.copy(navigationState = NavigationState.Idle) }
    }
}