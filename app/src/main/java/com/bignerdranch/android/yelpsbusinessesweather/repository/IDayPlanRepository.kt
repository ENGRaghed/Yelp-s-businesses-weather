package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan

interface IDayPlanRepository {

    suspend fun addDayPlan(dayPlan: DayPlan)
    suspend fun deleteDayPlan(dayPlan: DayPlan)
    suspend fun updateDayPlan(dayPlan: DayPlan)
    fun readAllDayPlan() : LiveData<List<DayPlan>>
    fun readDayPlan(id : String) : LiveData<DayPlan>
}