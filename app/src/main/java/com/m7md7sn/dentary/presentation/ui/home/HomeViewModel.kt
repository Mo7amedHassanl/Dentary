package com.m7md7sn.dentary.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.data.repository.ProfileRepository
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
    private val patientRepository: PatientRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                // Load user data
                val currentUser = authRepository.getCurrentUser()
                currentUser?.let { user ->
                    val metadata = user.userMetadata
                    val displayName = metadata?.get("display_name")?.toString()?.removeSurrounding("\"")
                        ?: metadata?.get("username")?.toString()?.removeSurrounding("\"")
                        ?: "مستخدم"
                    val clinic = metadata?.get("clinic_name")?.toString()?.removeSurrounding("\"")
                        ?: "عيادة الأسنان"

                    _uiState.value = _uiState.value.copy(
                        userName = displayName,
                        clinicName = clinic
                    )
                }

                // Load profile data including profile picture
                loadProfileData()

                // Load recent patients (limit to 4)
                loadRecentPatients()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    userName = "مستخدم",
                    clinicName = "عيادة الأسنان",
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadProfileData() {
        when (val result = profileRepository.getProfile()) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(
                    profilePictureUrl = result.data.profilePicture
                )
            }
            is Result.Error -> {
                // Keep existing profile picture URL if loading fails
                println("Failed to load profile data: ${result.message}")
            }
            is Result.Loading -> {
                // Keep existing state
            }
        }
    }

    private suspend fun loadRecentPatients() {
        when (val result = patientRepository.getAllPatients()) {
            is Result.Success -> {
                // Sort by creation date and take first 4 (most recent)
                val recentPatients = result.data
                    .sortedByDescending { it.createdAt ?: "0" }
                    .take(4)

                _uiState.value = _uiState.value.copy(
                    recentPatients = recentPatients,
                    isLoading = false,
                    errorMessage = null
                )
            }
            is Result.Error -> {
                _uiState.value = _uiState.value.copy(
                    recentPatients = emptyList(),
                    isLoading = false,
                    errorMessage = result.message
                )
            }
            is Result.Loading -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
        }
    }

    fun refreshData() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        loadHomeData()
    }

    fun onSeeAllPatientsClick(navigateToPatients: () -> Unit) {
        navigateToPatients()
    }
}