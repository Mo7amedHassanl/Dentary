//package com.m7md7sn.dentary.presentation.ui.auth
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.m7md7sn.dentary.data.model.LoginCredentials
//import com.m7md7sn.dentary.data.model.SignUpCredentials
//import com.m7md7sn.dentary.data.repository.AuthRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AuthViewModel @Inject constructor(
//    private val authRepository: AuthRepository
//): ViewModel() {
//    private val _uiState = MutableStateFlow(AuthUiState())
//    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
//
//    fun login(loginCredentials: LoginCredentials) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//            try {
//                val result = authRepository.login(loginCredentials)
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isAuthenticated = result.isSuccess,
//                        user = result.getOrNull(),
//                        error = null
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isAuthenticated = false,
//                        user = null,
//                        error = e.message
//                    )
//                }
//            }
//        }
//    }
//
//    fun register(signUpCredentials: SignUpCredentials) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//            try {
//                val result = authRepository.signUp(signUpCredentials)
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isAuthenticated = result.isSuccess,
//                        user = result.getOrNull(),
//                        error = null
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isAuthenticated = false,
//                        user = null,
//                        error = e.message
//                    )
//                }
//            }
//        }
//    }
//
//    fun signOut() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, error = null) }
//            try {
//                authRepository.signOut()
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        isAuthenticated = false,
//                        user = null,
//                        error = null
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        error = e.message,
//                    )
//                }
//            }
//        }
//    }
//}