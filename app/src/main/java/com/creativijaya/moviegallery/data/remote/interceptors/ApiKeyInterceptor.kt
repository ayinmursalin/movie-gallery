package com.creativijaya.moviegallery.data.remote.interceptors

import com.creativijaya.moviegallery.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.let {
            val request = it.request()
            val newUrl = request.url
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            val newChain = request
                .newBuilder()
                .url(newUrl)
                .build()

            it.proceed(newChain)
        }
    }
}
