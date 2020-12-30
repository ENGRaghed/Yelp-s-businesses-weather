package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.database.Dao
import com.bignerdranch.android.yelpsbusinessesweather.model.Weather
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.network.WeatherApi
import com.bignerdranch.android.yelpsbusinessesweather.network.YelpApi

private const val API_KEY = "fIK9HGPtNk-VJEAjIM4YyP0sRdeIpG82w6dnYVw_KsVz5c4RT54du50UT5uDakogcu8ism-9EeiEBc9Ca1014bzMMIejU6neWdmo3Zc6NePREOjcoY2XJ_p8SkTaX3Yx"

class Repository (private val yelpApi: YelpApi, private val weatherApi: WeatherApi, private val businessesDao: Dao) : IRepository {



    override val readAllBusinesses : LiveData<List<YelpRestaurant>> = businessesDao.readAllBusinesses()


    override suspend fun getYelpBusinesses(Autho : String, lat:String, lon:String) : List<YelpRestaurant> {
        businessesDao.deleteAllBusinesses()

        var businesses = yelpApi.searchRestaurants(Autho,lat,lon).businesses

        if (!businesses.isNullOrEmpty()) {


//        var businesses = businessesDao.

            businessesDao.addBusinesses(*businesses.map {
                YelpRestaurant(
                        it.yelpId,
                        it.id,
                        it.name,
                        it.rating,
                        it.price,
                        it.numReviews,
                        it.distanceInMeters,
                        it.imageUrl,
                        it.categories,
                        it.location,
                        it.coordinates,
                        it.phone
                )

            }.toTypedArray()
            )

        }

        return businesses
    }



    override suspend fun getYelpBusinessesByCategory(Autho : String, term : String, lat:String, lon:String) : List<YelpRestaurant> {
        businessesDao.deleteAllBusinesses()

        var businesses = yelpApi.searchBusinessesByCategory(Autho,term,lat,lon).businesses

        if (!businesses.isNullOrEmpty()) {


//        var businesses = businessesDao.
            businessesDao.addBusinesses(*businesses.map {
                YelpRestaurant(
                        it.yelpId,
                        it.id,
                        it.name,
                        it.rating,
                        it.price,
                        it.numReviews,
                        it.distanceInMeters,
                        it.imageUrl,
                        it.categories,
                        it.location,
                        it.coordinates,
                        it.phone
                )

            }.toTypedArray()
            )

        }

        return businesses
    }


    override suspend fun getWeather(key: String, latlon : String, days : String): Weather {
        return weatherApi.getCurrentWeather(key,latlon,days)
    }

    override fun readBusinesse(id : String) : LiveData<YelpRestaurant>{
        return businessesDao.readBusinesse(id)
    }

    override suspend fun fetchPhotos(id: String,Autho: String) : List<String>{
        return yelpApi.BusinessesDetails(id,Autho).photos
    }
//    suspend fun addDayPlan(dayPlan: DayPlan){
//        businessesDao.addDayPlan(dayPlan)
//    }
//
//    suspend fun deleteDayPlan(dayPlan: DayPlan){
//        businessesDao.deleteDayPlan(dayPlan)
//    }
//    suspend fun updateDayPlan(dayPlan: DayPlan){
//        businessesDao.updateDayPlan(dayPlan)
//    }
//
//    fun readAllDayPlan() : LiveData<List<DayPlan>> {
//        return businessesDao.readAllDayPlans()
//    }
//    suspend fun readDayPlan(id : Int) : LiveData<DayPlan> {
//       return businessesDao.readDayPlan(id)
//    }

}