package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.model.Weather
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant

interface IRepository {
    val readAllBusinesses : LiveData<List<YelpRestaurant>>

    suspend fun getYelpBusinesses(Autho : String, lat: String, lon: String) : List<YelpRestaurant>

    suspend fun getYelpBusinessesByCategory(Autho : String, term : String, lat: String, lon: String) : List<YelpRestaurant>

    suspend fun getWeather(key: String, latlon : String, days : String): Weather
    fun readBusinesse(id : String) : LiveData<YelpRestaurant>
}
