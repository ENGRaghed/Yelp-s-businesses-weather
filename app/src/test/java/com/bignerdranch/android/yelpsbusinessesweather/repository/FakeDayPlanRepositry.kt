package com.bignerdranch.android.yelpsbusinessesweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.yelpsbusinessesweather.model.Coordinates
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpCategory
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpLocation
import java.util.*
import kotlin.collections.ArrayList

class FakeDayPlanRepositry : IDayPlanRepository{

    private val dayPlansList = ArrayList<DayPlan>()
    private val dayPlans = MutableLiveData<List<DayPlan>>()

    fun add(){
        val category = mutableListOf<YelpCategory>()

        dayPlansList.add( DayPlan(
                0,
                "", 3.3,
                "",
                3,
                3.3,
                "", category
                , YelpLocation(""), Coordinates(0.0, 0.0), "",
                Date()
        ))
        dayPlansList.add( DayPlan(
                1,
                "", 3.3,
                "",
                3,
                3.3,
                "", category
                , YelpLocation(""), Coordinates(0.0, 0.0), "",
                Date()
        ))
        dayPlans.value = dayPlansList

    }
    override suspend fun addDayPlan(dayPlan: DayPlan) {
        dayPlansList.add(dayPlan)
        dayPlans.value = dayPlansList
    }

    override suspend fun deleteDayPlan(dayPlan: DayPlan) {
        dayPlansList.remove(dayPlan)
        dayPlans.value = dayPlansList
    }

    override suspend fun updateDayPlan(dayPlan: DayPlan) {
        var i =0
        dayPlansList.forEach {
            if (it.dayPlanId == dayPlan.dayPlanId){
                dayPlansList.set(i,dayPlan)
            }
            ++i
        }
        dayPlans.value = dayPlansList
    }

    override fun readAllDayPlan(): LiveData<List<DayPlan>> {
        return dayPlans
    }

    override fun readDayPlan(id: String): LiveData<DayPlan> {
        val dayPlan = MutableLiveData<DayPlan>()
        val index = dayPlansList.indexOfFirst {  it.dayPlanId == id.toInt() }
        dayPlan.value = dayPlansList[index]
        return dayPlan
    }

}