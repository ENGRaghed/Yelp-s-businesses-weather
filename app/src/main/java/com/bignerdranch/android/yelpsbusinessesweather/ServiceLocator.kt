package com.bignerdranch.android.yelpsbusinessesweather

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.yelpsbusinessesweather.database.BusinessesDatabase
import com.bignerdranch.android.yelpsbusinessesweather.network.WeatherApi
import com.bignerdranch.android.yelpsbusinessesweather.network.YelpApi
import com.bignerdranch.android.yelpsbusinessesweather.repository.DayPlanRepository
import com.bignerdranch.android.yelpsbusinessesweather.repository.Repository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private const val YELP_BASE_URL = "https://api.yelp.com/v3/"


object ServiceLocator {

    private lateinit var app: App
    lateinit var retrofit: Retrofit
    lateinit var yelpApi: YelpApi
    lateinit var weatherApi: WeatherApi
    lateinit var businessesDatabase: BusinessesDatabase

    fun init(app: App) {
        this.app = app
        initializeNetwork(app)
        initializeNetworkWeather(app)
        initializeDatabase(app)
    }


    private fun initializeNetwork(context: Context) {
        retrofit = Retrofit.Builder()
            .baseUrl(YELP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yelpApi = retrofit.create(YelpApi::class.java)
    }

    private fun initializeNetworkWeather(context: Context) {
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherApi = retrofit.create(WeatherApi::class.java)
    }

    fun initializeDatabase(context: Context) {
        businessesDatabase = Room.databaseBuilder(
            context.applicationContext,
            BusinessesDatabase::class.java,
            "businesses_database"
        ).build()
    }

    val repository : Repository by lazy {
        Repository(yelpApi, weatherApi, businessesDatabase.businessesDao())
    }

    val dayPlanRepository : DayPlanRepository by lazy {
        DayPlanRepository(
            businessesDatabase.daoDayPlan()
        )
    }

}