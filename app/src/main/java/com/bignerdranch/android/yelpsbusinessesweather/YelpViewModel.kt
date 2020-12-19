package com.bignerdranch.android.yelpsbusinessesweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class YelpViewModel : ViewModel() {
    val repository = ServiceLocator.repository

    fun getYelpBusinesses(Autho : String,lat:String,lon:String):LiveData<List<YelpRestaurant>>{
        val yelp = MutableLiveData<List<YelpRestaurant>>()
        viewModelScope.launch {
            yelp.value = repository.getYelpBusinesses(Autho,lat,lon)
        }
        return yelp
    }
}