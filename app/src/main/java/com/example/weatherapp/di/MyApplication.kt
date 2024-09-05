package com.example.weatherapp.di

import android.app.Application
import android.graphics.Bitmap
import com.example.weatherapp.R
import com.example.weatherapp.di.module.apiModule
import com.example.weatherapp.di.module.repositoryModule
import com.example.weatherapp.di.module.useCaseModule
import com.example.weatherapp.di.module.viewmodelModule
import com.example.weatherapp.utils.common.LoadImage
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.utils.L
import com.nostra13.universalimageloader.utils.StorageUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication:Application() {
    companion object {
        lateinit var instance: MyApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        initImageLoader()
        instance = this
        LoadImage.init(this)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(listOf(repositoryModule, viewmodelModule, apiModule, useCaseModule))
        }
    }

    private fun initImageLoader() = try {
        val cacheDir = StorageUtils.getOwnCacheDirectory(applicationContext, "OMW")
        val options = DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.icon_app)
            .showImageOnLoading(R.drawable.icon_app)
            .showImageOnFail(R.drawable.icon_app)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .build()

        val config = ImageLoaderConfiguration.Builder(applicationContext)
            .diskCache(UnlimitedDiskCache(cacheDir))
            .defaultDisplayImageOptions(options)
            .build()
        ImageLoader.getInstance().init(config)
        L.writeLogs(false)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}