package com.tapan.avomatest.ui.custom

interface LoadingView {
    fun onStartLoading()
    fun onStopLoading(success: Boolean, message: String = "")
    fun onInit()

}