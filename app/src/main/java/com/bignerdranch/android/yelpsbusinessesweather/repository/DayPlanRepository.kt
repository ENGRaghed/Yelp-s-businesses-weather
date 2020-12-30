package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.database.DaoDayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan

class DayPlanRepository (private val daoDayPlan: DaoDayPlan): IDayPlanRepository{

    override suspend fun addDayPlan(dayPlan: DayPlan){
        daoDayPlan.addDayPlan(dayPlan)
    }

    override suspend fun deleteDayPlan(dayPlan: DayPlan){
        daoDayPlan.deleteDayPlan(dayPlan)
    }
    override suspend fun updateDayPlan(dayPlan: DayPlan){
        daoDayPlan.updateDayPlan(dayPlan)
    }

    override fun readAllDayPlan() : LiveData<List<DayPlan>> {
        return daoDayPlan.readAllDayPlans()
    }
    override fun readDayPlan(id : String) : LiveData<DayPlan> {
        return daoDayPlan.readDayPlan(id)
    }

}