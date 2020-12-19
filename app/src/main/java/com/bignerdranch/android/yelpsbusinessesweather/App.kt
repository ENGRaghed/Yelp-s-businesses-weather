package com.bignerdranch.android.yelpsbusinessesweather

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}