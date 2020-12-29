package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.yelpsbusinessesweather.model.*
import java.util.ArrayList

class FakeYelpRepositry : IRepository{

    val yelpRestaurantLiveData = MutableLiveData<List<YelpRestaurant>>()
    val yelpRestaurantList = ArrayList<YelpRestaurant>()
    val yelpRestaurant = MutableLiveData<YelpRestaurant>()





    val current = Current(0, Condition("",""),0.0,0.0,0.0,0,0,"",0,0.0,0)
    val hour = listOf(Hour("","",Condition("",""),0.0,"",0),Hour("","",Condition("",""),0.0,"",0))
    val day = Day(0.0, Condition("",""),"","",0,0,1.1,0.0)
    val forecastday = listOf<Forecastday>(Forecastday("",123,day,hour),Forecastday("",0,day,hour))
    val forecast = Forecast(forecastday)
    val weather = Weather(current,forecast,Location("",0.0,"",0,0.0,"",""))

    val category = mutableListOf<YelpCategory>()


    val yelpRestaurant1 =YelpRestaurant( 0,
            "jjjjj","Kim's Sushi",
            "",
            4.5,
            "$$",
            false,
            812,
            3.3,
            "https://s3-media4.fl.yelpcdn.com/bphoto/FU1dpikJj_A49HUTg16smA/o.jpg", category
            , YelpLocation("458 Eagle Rock Ave"), Coordinates(40.8034302, -74.2490792),emptyList<String>())
    val yelpRestaurant2 =YelpRestaurant( 1,
            "jkjjkj","Essex County Turtle Back Zoo", "",4.0,
            "$$",
            false,
            331,
            3.3,
            "", category
            , YelpLocation("458 Eagle Rock Ave"), Coordinates(40.8034302, -74.2490792), emptyList<String>())


    fun add(){
        yelpRestaurantList.add(yelpRestaurant1)
        yelpRestaurantList.add(yelpRestaurant2)

        yelpRestaurantLiveData.value = yelpRestaurantList

    }

    override val readAllBusinesses: LiveData<List<YelpRestaurant>> = yelpRestaurantLiveData

    override suspend fun getYelpBusinesses(Autho: String, lat: String, lon: String): List<YelpRestaurant> {
        return yelpRestaurantList
    }

    override suspend fun getYelpBusinessesByCategory(Autho: String, term: String, lat: String, lon: String): List<YelpRestaurant> {
        return yelpRestaurantList
    }

    override suspend fun getWeather(key: String, latlon: String, days: String): Weather {
        return weather
    }

    override fun readBusinesse(id: String): LiveData<YelpRestaurant> {
        val index = yelpRestaurantList.indexOfFirst { it.yelpId == id.toInt() }
        yelpRestaurant.value = yelpRestaurantList[index]
        return yelpRestaurant
    }

}