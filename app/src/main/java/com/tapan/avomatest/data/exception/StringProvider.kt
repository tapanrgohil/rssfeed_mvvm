package com.tapan.avomatest.data.exception

import android.content.Context
import androidx.annotation.StringRes
import com.tapan.avomatest.R
import javax.inject.Inject

interface StringProvider {
    fun messageForStringResource(@StringRes int: Int): String
    fun getErrorCodeMessageRes(@StringRes int: Int): Int
}

class StringProviderImpl @Inject constructor(
    private val
    context: Context
) : StringProvider {
    companion object {
        private val errorCodeStringMap = HashMap<Int, Int>()
    }

    init {
        synchronized(this) {
            errorCodeStringMap[HTTP_400_BAD_REQUEST] = R.string.bad_request
            errorCodeStringMap[HTTP_401_UNAUTHORIZED] = R.string.unauthorized_error

        }
    }

    override fun messageForStringResource(int: Int): String {
        return try {
            context.getString(int)
        } catch (e: Exception) {
            return "Some thing went wrong"
        }
    }

    override fun getErrorCodeMessageRes(int: Int): Int {
        return errorCodeStringMap[int]?:505
    }
}