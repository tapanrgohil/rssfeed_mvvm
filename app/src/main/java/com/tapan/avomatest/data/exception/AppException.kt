package com.tapan.avomatest.data.exception

import com.tapan.avomatest.R
import com.tapan.avomatest.RSSApp
import retrofit2.HttpException

const val HTTP_400_BAD_REQUEST = 400
const val HTTP_401_UNAUTHORIZED = 401
const val UNKNOWN_HTTP_EXCEPTION = 1006

class AppException : RuntimeException {

    var errorCode: Int = 0
    private var throwable: Throwable? = null
    private var errorMessage: String? = null

    constructor(errorCode: Int, prvider: StringProvider? = null) : super() {
        var stringProvider: StringProvider? = prvider
        if (prvider == null) {
            stringProvider = StringProviderImpl(RSSApp.instance)
        }
        this.errorCode = errorCode
        errorMessage = if (stringProvider?.getErrorCodeMessageRes(errorCode) == null) super.message
        else stringProvider.messageForStringResource(
            stringProvider.getErrorCodeMessageRes(
                errorCode
            )
        )
    }

    constructor(errorCode: Int, message: String) : super() {
        this.errorCode = errorCode
        this.errorMessage = message
    }

    constructor(throwable: Throwable?, prvider: StringProvider? = null) : super(throwable) {
        this.throwable = throwable
        var stringProvider: StringProvider? = prvider
        if (prvider == null) {
            stringProvider = StringProviderImpl(RSSApp.instance)
        }

        /**
         * Higher priority exceptions should be placed higher in the when statement. If an exception
         * is a subclass of multiple cases, the first case would be selected.
         */
        when (throwable) {
            is AppException -> {
                this.errorCode = throwable.errorCode
                this.errorMessage = throwable.errorMessage
                this.throwable = throwable.throwable
            }
            is HttpException -> errorCode = when (throwable.code()) {
                400 -> HTTP_400_BAD_REQUEST
                401 -> HTTP_401_UNAUTHORIZED
                else -> UNKNOWN_HTTP_EXCEPTION
            }
        }

        val errorCodeMessageRes = stringProvider?.getErrorCodeMessageRes(errorCode)
        if (errorCodeMessageRes != null) {
            errorMessage = stringProvider?.messageForStringResource(errorCodeMessageRes)
        } else if (errorMessage == null) {
            errorMessage = stringProvider?.messageForStringResource(R.string.unknown_error)
                ?: "Unknown Error"
        }
    }

    override val cause: Throwable?
        get() = throwable


    override val message: String
        get() = errorMessage?.let { "$it: $errorCode" } ?: ""


}
