package com.tapan.avomatest.data.base

import retrofit2.Response

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null,
    val retrofitResponse: Response<*>? = null,
    var handled: Boolean = false
) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> error(
            message: String?,
            data: T? = null,
            throwable: Throwable? = null,
            retrofitResponse: Response<*>? = null
        ): Resource<T> {
            return Resource(Status.ERROR, data, message, throwable, retrofitResponse)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, handled = false)
        }
    }
}

sealed class Status {
    object SUCCESS : Status()
    object ERROR : Status()
    object LOADING : Status()
}