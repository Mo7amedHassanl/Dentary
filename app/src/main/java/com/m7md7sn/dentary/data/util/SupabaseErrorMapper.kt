package com.m7md7sn.dentary.data.util

import com.m7md7sn.dentary.domain.model.DataError
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException

fun Exception.toDataError(): DataError {
    return when (this) {
        is HttpRequestTimeoutException -> DataError.Network.REQUEST_TIMEOUT
        is UnknownHostException -> DataError.Network.NO_INTERNET
        is SerializationException -> DataError.Network.SERIALIZATION
        is ClientRequestException -> {
            when (response.status.value) {
                401 -> DataError.Auth.SESSION_EXPIRED
                403 -> DataError.Auth.SESSION_EXPIRED
                404 -> DataError.Network.UNKNOWN
                409 -> DataError.Auth.USER_ALREADY_EXISTS
                else -> DataError.Network.UNKNOWN
            }
        }
        is ServerResponseException -> DataError.Network.SERVER_ERROR
        is RestException -> DataError.Network.UNKNOWN
        else -> DataError.Network.UNKNOWN
    }
}