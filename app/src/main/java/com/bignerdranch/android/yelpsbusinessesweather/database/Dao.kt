package com.bignerdranch.android.yelpsbusinessesweather.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant

@Dao
interface Dao{
    @Insert
    suspend fun addBusinesses(vararg businesses: YelpRestaurant)

    @Query("DELETE FROM businesses_table")
    suspend fun deleteAllBusinesses()

    @Query("SELECT * FROM businesses_table")
    fun readAllBusinesses():LiveData<List<YelpRestaurant>>

    @Query("SELECT * FROM businesses_table WHERE yelpId = :id")
    fun readBusinesse(id : String):LiveData<YelpRestaurant>

}