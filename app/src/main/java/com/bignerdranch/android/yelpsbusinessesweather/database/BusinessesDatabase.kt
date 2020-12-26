package com.bignerdranch.android.yelpsbusinessesweather.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.yelpsbusinessesweather.TypeConverter
import com.bignerdranch.android.yelpsbusinessesweather.model.DayPlan
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant

@Database(entities = [YelpRestaurant::class,DayPlan::class],version = 1)
@TypeConverters(TypeConverter::class)
abstract class BusinessesDatabase : RoomDatabase() {

    abstract fun businessesDao() : Dao
    abstract fun daoDayPlan() : DaoDayPlan

}