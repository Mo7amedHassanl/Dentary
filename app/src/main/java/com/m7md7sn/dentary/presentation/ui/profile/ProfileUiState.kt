package com.m7md7sn.dentary.presentation.ui.profile

import com.m7md7sn.dentary.data.model.Profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val userEmail: String? = null,
    val totalPatients: Int = 0,
    val error: String? = null,
    val isError: Boolean = false
)