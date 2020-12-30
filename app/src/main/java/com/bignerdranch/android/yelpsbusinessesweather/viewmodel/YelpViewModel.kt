package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.yelpsbusinessesweather.ServiceLocator
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.Weather
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.bignerdranch.android.yelpsbusinessesweather.repository.IRepository
import com.bignerdranch.android.yelpsbusinessesweather.repository.Repository
import kotlinx.coroutines.launch

class YelpViewModel(val repository : IRepository) : ViewModel() {

    val readAllBusinesses : LiveData<List<YelpRestaurant>> = repository.readAllBusinesses

    fun getYelpBusinesses(Autho : String,lat:String,lon:String):LiveData<List<YelpRestaurant>>{
        val yelp = MutableLiveData<List<YelpRestaurant>>()
        viewModelScope.launch {
            yelp.value = repository.getYelpBusinesses(Autho,lat,lon)
        }
        return yelp
    }
    fun getYelpBusinessesByCategory(Autho : String, term : String ,lat:String,lon:String):LiveData<List<YelpRestaurant>>{
        val yelp = MutableLiveData<List<YelpRestaurant>>()
        viewModelScope.launch {
            yelp.value = repository.getYelpBusinessesByCategory(Autho,term,lat,lon)
        }
        return yelp
    }

    fun getCurrentWeather(key : String,latlon : String,days:String):LiveData<Weather>{
        val weather = MutableLiveData<Weather>()
        viewModelScope.launch {
            weather.value = repository.getWeather(key,latlon,days)
        }
        return weather
    }
    fun readBusinesse(id: String):LiveData<YelpRestaurant>{
        return repository.readBusinesse(id)
    }

    fun getPhotos(id : String,auth : String):LiveData<List<String>>{
        val weather = MutableLiveData<List<String>>()
        viewModelScope.launch {
            weather.value = repository.fetchPhotos(id,auth)
        }
        return weather
    }





}