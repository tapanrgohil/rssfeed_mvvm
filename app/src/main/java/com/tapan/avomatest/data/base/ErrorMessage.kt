package com.tapan.avomatest.data.base


import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: Int
)