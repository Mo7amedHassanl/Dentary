package com.m7md7sn.dentary.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.ProfileRepository
import com.m7md7sn.dentary.domain.usecase.patient.GetPatientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.m7md7sn.dentary.utils.Result

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val getPatientsUseCase: GetPatientsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observePatients()
        loadHomeData()
    }

    private fun observePatients() {
        viewModelScope.launch {
            getPatientsUseCase(sortByRecent = true).collect { result ->
                handlePatientsResult(result)
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                // Fetch profile
                val profileResult = profileRepository.getProfile()
                handleProfileResult(profileResult)
                
                // Trigger patients refresh (the observer will update the UI)
                getPatientsUseCase.refresh()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun handleProfileResult(result: Result<com.m7md7sn.dentary.data.model.Profile, com.m7md7sn.dentary.domain.model.DataError>) {
        when (result) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(
                    userName = result.data.fullName ?: "مستخدم",
                    clinicName = result.data.clinicName ?: "عيادة الأسنان",
                    profilePictureUrl = result.data.profilePicture
                )
            }
            is Result.Error -> {
                // If profile fails, try getting name from metadata as fallback
                val currentUser = authRepository.getCurrentUser()
                val metadata = currentUser?.userMetadata
                val displayName = metadata?.get("display_name")?.toString()?.removeSurrounding("\"")
                    ?: metadata?.get("username")?.toString()?.removeSurrounding("\"")
                    ?: "مستخدم"
                
                _uiState.value = _uiState.value.copy(
                    userName = displayName,
                    errorMessage = result.message
                )
            }
            else -> {}
        }
    }

    private fun handlePatientsResult(result: Result<List<com.m7md7sn.dentary.data.model.Patient>, com.m7md7sn.dentary.domain.model.DataError>) {
        when (result) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(
                    recentPatients = result.data.take(4)
                )
            }
            is Result.Error -> {
                _uiState.value = _uiState.value.copy(
                    recentPatients = emptyList(),
                    errorMessage = result.message
                )
            }
            else -> {}
        }
    }

    fun refreshData() {
        loadHomeData()
    }

    fun onSeeAllPatientsClick(navigateToPatients: () -> Unit) {
        navigateToPatients()
    }
}