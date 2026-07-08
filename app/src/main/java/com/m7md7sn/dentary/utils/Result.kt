package com.m7md7sn.dentary.utils

import com.m7md7sn.dentary.domain.model.Error

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E: com.m7md7sn.dentary.domain.model.Error>(val error: E, val message: String? = null): Result<Nothing, E>
    data object Loading : Result<Nothing, Nothing>
}

