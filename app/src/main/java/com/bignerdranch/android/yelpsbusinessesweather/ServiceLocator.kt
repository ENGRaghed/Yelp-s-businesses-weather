package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private const val YELP_BASE_URL = "https://api.yelp.com/v3/"


object ServiceLocator {

    private lateinit var app: App
    lateinit var retrofit: Retrofit
    lateinit var yelpApi: YelpApi

    fun init(app: App) {
        this.app = app
        initializeNetwork(app)
    }


    private fun initializeNetwork(context: Context) {
        retrofit = Retrofit.Builder()
            .baseUrl(YELP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yelpApi = retrofit.create(YelpApi::class.java)
    }

    val repository : Repository by lazy {
        Repository(yelpApi)
    }

}