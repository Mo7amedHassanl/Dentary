package com.m7md7sn.dentary.presentation.ui.auth.passwordreset

import com.m7md7sn.dentary.utils.Result

data class PasswordResetUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val passwordResetResult: Result<Unit>? = null
)

