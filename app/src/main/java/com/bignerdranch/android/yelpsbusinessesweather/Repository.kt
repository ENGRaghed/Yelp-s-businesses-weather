package com.bignerdranch.android.yelpsbusinessesweather

class Repository (private val yelpApi: YelpApi,private val weatherApi: WeatherApi){

    suspend fun getYelpBusinesses(Autho : String,lat:String,lon:String) : List<YelpRestaurant> {
        return yelpApi.searchRestaurants(Autho,lat,lon).businesses
    }

    suspend fun getWeather(key: String,latlon : String,days : String): Weather {
        return weatherApi.getCurrentWeather(key,latlon,days)
    }
}