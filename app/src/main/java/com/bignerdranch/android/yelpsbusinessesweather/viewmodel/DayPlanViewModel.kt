package com.bignerdranch.android.yelpsbusinessesweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.yelpsbusinessesweather.ServiceLocator
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.repository.DayPlanRepository
import com.bignerdranch.android.yelpsbusinessesweather.repository.IDayPlanRepository
import kotlinx.coroutines.launch

class DayPlanViewModel(private val repository : IDayPlanRepository) : ViewModel(){
//    val repository = ServiceLocator.dayPlanRepository

    fun addDayPlan(dayPlan: DayPlan){
        viewModelScope.launch {
            repository.addDayPlan(dayPlan)
        }
    }

    fun deleteDayPlan(dayPlan: DayPlan){
        viewModelScope.launch {
            repository.deleteDayPlan(dayPlan)
        }
    }
    fun updateDayPlan(dayPlan: DayPlan)  {
        viewModelScope.launch {
            repository.updateDayPlan(dayPlan)
        }
    }

    fun readDayPlan(id : String) : LiveData<DayPlan> {
           return repository.readDayPlan(id)
    }

    val readAllDayPlan : LiveData<List<DayPlan>> = repository.readAllDayPlan()


}