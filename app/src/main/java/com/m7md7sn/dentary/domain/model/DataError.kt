package com.m7md7sn.dentary.domain.model

sealed interface Error

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
    
    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }
    
    enum class Auth: DataError {
        INVALID_CREDENTIALS,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        SESSION_EXPIRED,
        UNKNOWN
    }
}