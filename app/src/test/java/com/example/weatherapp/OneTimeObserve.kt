package com.example.weatherapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer

//class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
//    val lifecycle = LifecycleRegistry(this)
//    init {
//        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    }
//
//     fun getLifecycle(): Lifecycle = lifecycle
//
//    override fun onChanged(t: T) {
//        handler(t)
//        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    }
//}