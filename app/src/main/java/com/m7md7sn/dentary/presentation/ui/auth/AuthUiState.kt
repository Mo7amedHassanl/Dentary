package com.m7md7sn.dentary.presentation.ui.auth

import io.github.jan.supabase.auth.user.UserInfo

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: UserInfo? = null,
    val error: String? = null
)
