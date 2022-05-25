package com.tapan.avomatest.ui.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseState<T>(
    var isLoading: Boolean = false,
    var throwable: Throwable? = null,
    var error: String? = null
) : Parcelable {
    fun clearError() {
        error = null;
    }
}