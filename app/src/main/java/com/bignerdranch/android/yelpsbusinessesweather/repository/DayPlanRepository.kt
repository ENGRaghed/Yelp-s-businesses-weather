package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.database.DaoDayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan

class DayPlanRepository (private val daoDayPlan: DaoDayPlan){
    suspend fun addDayPlan(dayPlan: DayPlan){
        daoDayPlan.addDayPlan(dayPlan)
    }

    suspend fun deleteDayPlan(dayPlan: DayPlan){
        daoDayPlan.deleteDayPlan(dayPlan)
    }
    suspend fun updateDayPlan(dayPlan: DayPlan){
        daoDayPlan.updateDayPlan(dayPlan)
    }

    fun readAllDayPlan() : LiveData<List<DayPlan>> {
        return daoDayPlan.readAllDayPlans()
    }
    fun readDayPlan(id : String) : LiveData<DayPlan> {
        return daoDayPlan.readDayPlan(id)
    }

}