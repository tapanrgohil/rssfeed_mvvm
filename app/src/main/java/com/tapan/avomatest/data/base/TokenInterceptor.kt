package com.tapan.avomatest.data.base

import com.tapan.avomatest.GlobalVar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl: HttpUrl = original.url
        val url = originalUrl.newBuilder()
            .addQueryParameter(
                "api_key",
                GlobalVar.API_KEY
            )
            .build()
        val requestBuilder: Request.Builder = original.newBuilder().url(url)

        return chain.proceed(requestBuilder.build())

    }

}