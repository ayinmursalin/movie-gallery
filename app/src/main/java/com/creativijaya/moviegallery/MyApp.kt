package com.creativijaya.moviegallery

import android.app.Application
import com.creativijaya.moviegallery.di.remoteSourceModule
import com.creativijaya.moviegallery.di.useCaseModule
import com.creativijaya.moviegallery.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MyApp)
            modules(
                remoteSourceModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
