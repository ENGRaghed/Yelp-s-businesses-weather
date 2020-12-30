package com.bignerdranch.android.yelpsbusinessesweather.network

import com.bignerdranch.android.yelpsbusinessesweather.model.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json?")
    suspend fun getCurrentWeather(@Query("key") authHeader: String,
                                  @Query("q") latlon:String,
                                  @Query("days") days : String) : Weather
}