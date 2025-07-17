package com.m7md7sn.dentary.presentation.ui.auth.emailverification

import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.user.UserInfo

data class EmailVerificationUiState(
    val email: String = "",
    val otpCode: String = "",
    val otpError: String? = null,
    val isLoading: Boolean = false,
    val isResending: Boolean = false,
    val verificationResult: Result<UserInfo>? = null,
    val resendResult: Result<Unit>? = null,
    val canResend: Boolean = true,
    val resendCountdown: Int = 0
)
