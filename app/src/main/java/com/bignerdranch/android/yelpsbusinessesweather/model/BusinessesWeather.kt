package com.bignerdranch.android.yelpsbusinessesweather.model

import androidx.room.Entity

data class BusinessesWeather (
        val yelpBusinesses: YelpRestaurant,
        val weather: Weather)