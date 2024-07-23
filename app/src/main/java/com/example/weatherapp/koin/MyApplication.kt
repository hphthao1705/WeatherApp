package com.example.weatherapp.koin

import android.app.Application
import com.example.weatherapp.koin.module.apiModule
import com.example.weatherapp.koin.module.repositoryModule
import com.example.weatherapp.koin.module.useCaseModule
import com.example.weatherapp.koin.module.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(listOf(repositoryModule, viewmodelModule, apiModule, useCaseModule))
        }
    }
}