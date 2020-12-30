package com.bignerdranch.android.yelpsbusinessesweather.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan

@Dao
interface DaoDayPlan {

    @Insert
    suspend fun addDayPlan(dayPlan: DayPlan)

    @Update
    suspend fun updateDayPlan(dayPlan: DayPlan)

    @Delete
    suspend fun deleteDayPlan(dayPlan: DayPlan)

    @Query("SELECT * FROM day_plan_table ORDER BY dayPlanId ASC")
    fun readAllDayPlans(): LiveData<List<DayPlan>>

    @Query("SELECT * FROM day_plan_table WHERE dayPlanId = :id")
     fun readDayPlan(id : String): LiveData<DayPlan>

}