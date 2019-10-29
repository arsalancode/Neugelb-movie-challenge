package com.neugelb.challenge.view

import android.app.Application
import com.neugelb.challenge.dh.BaseDependencies
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }

    private fun initDI() {
        startKoin {
            // Android context
            androidContext(this@MoviesApplication)
            // modules
            modules(BaseDependencies.networkingModule)
        }
    }
}