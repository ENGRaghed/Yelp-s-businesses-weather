package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.yelpsbusinessesweather.repository.IRepository
import com.bignerdranch.android.yelpsbusinessesweather.repository.Repository

class YelpViewModelFactory (private val repository: IRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YelpViewModel::class.java)) {
            return YelpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}