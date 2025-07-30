package com.m7md7sn.dentary.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.repository.AuthRepository
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.data.repository.ProfileRepository
import com.m7md7sn.dentary.utils.Event
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<Event<String>>()
    val snackbarMessage: SharedFlow<Event<String>> = _snackbarMessage.asSharedFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isError = false)

            try {
                // Load current user info
                val currentUser = authRepository.getCurrentUser()
                val userEmail = currentUser?.email

                // Load profile data
                val profileResult = profileRepository.getProfile()

                // Load total patients count
                val patientsResult = patientRepository.getAllPatients()
                val totalPatients = when (patientsResult) {
                    is Result.Success -> patientsResult.data.size
                    is Result.Error -> 0
                    else -> 0
                }

                // Load medical procedure statistics
                val statsResult = patientRepository.getMedicalProcedureStats()
                val medicalProcedureStats = when (statsResult) {
                    is Result.Success -> statsResult.data
                    is Result.Error -> emptyList()
                    else -> emptyList()
                }

                when (profileResult) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            profile = profileResult.data,
                            userEmail = userEmail,
                            totalPatients = totalPatients,
                            medicalProcedureStats = medicalProcedureStats,
                            isError = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            error = profileResult.message ?: "Unknown error occurred",
                            userEmail = userEmail,
                            totalPatients = totalPatients,
                            medicalProcedureStats = medicalProcedureStats
                        )
                        _snackbarMessage.emit(Event("Failed to load profile data"))
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isError = true,
                            error = "Unknown error occurred",
                            userEmail = userEmail,
                            totalPatients = totalPatients,
                            medicalProcedureStats = medicalProcedureStats
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    error = e.message ?: "Unknown error occurred"
                )
                _snackbarMessage.emit(Event("Error loading profile data"))
            }
        }
    }

    fun refreshProfile() {
        loadProfileData()
    }

    fun onEditProfileClick() {
        // TODO: Navigate to edit profile screen
        viewModelScope.launch {
            _snackbarMessage.emit(Event("Edit profile functionality coming soon"))
        }
    }

    fun onSeeAllPatientsClick(navigateToPatients: () -> Unit) {
        navigateToPatients()
    }

    fun updateProfilePicture(imageUri: android.net.Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Get the current profile picture URL to delete it
                val currentProfilePictureUrl = _uiState.value.profile?.profilePicture
                
                val result = profileRepository.updateProfilePicture(imageUri, currentProfilePictureUrl)
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            profile = result.data
                        )
                        _snackbarMessage.emit(Event("Profile picture updated successfully"))
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _snackbarMessage.emit(Event("Failed to update profile picture: ${result.message}"))
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _snackbarMessage.emit(Event("Unknown error occurred"))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _snackbarMessage.emit(Event("Error updating profile picture: ${e.message}"))
            }
        }
    }
}