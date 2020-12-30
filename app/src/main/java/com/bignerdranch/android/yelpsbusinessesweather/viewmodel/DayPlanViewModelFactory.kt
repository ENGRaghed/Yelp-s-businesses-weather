package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.yelpsbusinessesweather.repository.DayPlanRepository
import com.bignerdranch.android.yelpsbusinessesweather.repository.IDayPlanRepository

class DayPlanViewModelFactory (private val repository : IDayPlanRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayPlanViewModel::class.java)) {
            return DayPlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}