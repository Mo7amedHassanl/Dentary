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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
                coroutineScope {
                    val profileDeferred = async { profileRepository.getProfile() }
                    val patientsDeferred = async { patientRepository.getAllPatients() }
                    val statsDeferred = async { patientRepository.getMedicalProcedureStats() }
                    
                    val profileResult = profileDeferred.await()
                    val patientsResult = patientsDeferred.await()
                    val statsResult = statsDeferred.await()
                    
                    val currentUser = authRepository.getCurrentUser()
                    
                    val totalPatients = if (patientsResult is Result.Success) patientsResult.data.size else 0
                    val medicalProcedureStats = if (statsResult is Result.Success) statsResult.data else emptyList()

                    when (profileResult) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                profile = profileResult.data,
                                userEmail = currentUser?.email,
                                totalPatients = totalPatients,
                                medicalProcedureStats = medicalProcedureStats,
                                isError = false
                            )
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isError = true,
                                error = profileResult.message ?: "Failed to load profile",
                                userEmail = currentUser?.email,
                                totalPatients = totalPatients,
                                medicalProcedureStats = medicalProcedureStats
                            )
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun refreshProfile() {
        loadProfileData()
    }

    fun onEditProfileClick() {
        // Handled by navigation in the screen
    }

    fun onSeeAllPatientsClick(navigateToPatients: () -> Unit) {
        navigateToPatients()
    }

    fun updateProfilePicture(imageUri: android.net.Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
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
                        _snackbarMessage.emit(Event(result.message ?: "Failed to update profile picture"))
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _snackbarMessage.emit(Event("Error updating profile picture: ${e.message}"))
            }
        }
    }
}