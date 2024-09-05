package com.example.weatherapp.utils.common

import android.app.Application
import com.example.weatherapp.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

object LoadImage {
    private val imageLoader = ImageLoader.getInstance()
    private var options: DisplayImageOptions? = null

    private lateinit var application: Application

    fun init(application: Application) {
        this.application = application
    }

    fun getImageLLoader(): ImageLoader {
        this.imageLoader.init(ImageLoaderConfiguration.createDefault(application))

        return imageLoader
    }

    fun getImageOptions(): DisplayImageOptions {
        options = DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .showImageForEmptyUri(R.drawable.icon_app)
            .showImageOnFail(R.drawable.icon_app)
            .build()

        return options!!
    }

    fun getImageLOptions(): DisplayImageOptions {
        options = DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build()

        return options!!
    }
}