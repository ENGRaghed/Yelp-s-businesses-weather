package com.bignerdranch.android.yelpsbusinessesweather

class Repository (private val yelpApi: YelpApi){

    suspend fun getYelpBusinesses(Autho : String,lat:String,lon:String) : List<YelpRestaurant> {
        return yelpApi.searchRestaurants(Autho,lat,lon).businesses
    }
}