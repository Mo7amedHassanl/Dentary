package com.m7md7sn.dentary.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun navigateToScreen(screen: SettingsScreen) {
        _uiState.value = uiState.value.copy(currentScreen = screen)
    }

    fun navigateBack() {
        _uiState.value = uiState.value.copy(currentScreen = SettingsScreen.Main)
    }

    fun showLogoutConfirmDialog() {
        _uiState.value = uiState.value.copy(showLogoutConfirmDialog = true)
    }

    fun hideLogoutConfirmDialog() {
        _uiState.value = uiState.value.copy(showLogoutConfirmDialog = false)
    }

    fun confirmLogout(onLogoutSuccess: () -> Unit) {
        hideLogoutConfirmDialog()
        logout(onLogoutSuccess)
    }

    private fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)

            when (val result = authRepository.signOut()) {
                is Result.Success -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    onLogoutSuccess()
                }
                is Result.Error -> {
                    _uiState.value = uiState.value.copy(isLoading = false)
                    // Handle error if needed - for now we'll still call onLogoutSuccess
                    // since local logout should always work
                    onLogoutSuccess()
                }
                is Result.Loading -> {
                    // Loading state is already set above
                }
            }
        }
    }

    init {
        _uiState.value = SettingsUiState(currentScreen = SettingsScreen.Main)
    }
}