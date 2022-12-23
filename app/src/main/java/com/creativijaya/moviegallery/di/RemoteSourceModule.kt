package com.creativijaya.moviegallery.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.creativijaya.moviegallery.BuildConfig
import com.creativijaya.moviegallery.data.remote.interceptors.ApiKeyInterceptor
import com.creativijaya.moviegallery.data.remote.services.MovieService
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteSourceModule = module {

    single {
        Cache(get<Application>().cacheDir, 10 * 1024 * 1024L)
    }

    single {
        OkHttpClient.Builder()
            .cache(get())
            .followSslRedirects(true)
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<MovieService> {
        get<Retrofit>().create(MovieService::class.java)
    }

}
