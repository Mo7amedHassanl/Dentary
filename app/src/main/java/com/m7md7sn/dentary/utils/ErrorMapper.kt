package com.m7md7sn.dentary.utils

import com.m7md7sn.dentary.domain.model.DataError

fun DataError.asUiText(): String {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> "Request timed out. Please try again."
        DataError.Network.NO_INTERNET -> "No internet connection."
        DataError.Network.SERVER_ERROR -> "Server error. Please try again later."
        DataError.Network.SERIALIZATION -> "Data processing error."
        DataError.Network.UNKNOWN -> "An unknown network error occurred."
        
        DataError.Local.DISK_FULL -> "Storage is full."
        DataError.Local.UNKNOWN -> "An unknown local error occurred."
        
        DataError.Auth.INVALID_CREDENTIALS -> "Invalid email or password."
        DataError.Auth.USER_NOT_FOUND -> "User not found."
        DataError.Auth.USER_ALREADY_EXISTS -> "An account with this email already exists."
        DataError.Auth.SESSION_EXPIRED -> "Session expired. Please log in again."
        DataError.Auth.UNKNOWN -> "Authentication failed."
        else -> "An error occurred."
    }
}