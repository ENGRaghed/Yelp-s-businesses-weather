package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.Dao
import com.bignerdranch.android.yelpsbusinessesweather.model.Weather
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.network.WeatherApi
import com.bignerdranch.android.yelpsbusinessesweather.network.YelpApi

class Repository (private val yelpApi: YelpApi, private val weatherApi: WeatherApi , private val businessesDao: Dao){



    val readAllBusinesses : LiveData<List<YelpRestaurant>> = businessesDao.readAllBusinesses()


    suspend fun getYelpBusinesses(Autho : String,lat:String,lon:String) : List<YelpRestaurant> {
        businessesDao.deleteAllBusinesses()

        var businesses = yelpApi.searchRestaurants(Autho,lat,lon).businesses

//        var businesses = businessesDao.
        businessesDao.addBusinesses(*businesses.map {
            YelpRestaurant(
                it.yelpId,
                it.name,
                it.rating,
                it.price,
                it.numReviews,
                it.distanceInMeters,
                it.imageUrl,
                it.categories,
                it.location,
                it.coordinates
            )

        }.toTypedArray()
        )



        return businesses
    }

    suspend fun getWeather(key: String,latlon : String,days : String): Weather {
        return weatherApi.getCurrentWeather(key,latlon,days)
    }

}