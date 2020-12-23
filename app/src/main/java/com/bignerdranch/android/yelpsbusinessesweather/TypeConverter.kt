package com.bignerdranch.android.yelpsbusinessesweather

import androidx.room.TypeConverter
import com.bignerdranch.android.yelpsbusinessesweather.model.Coordinates
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpCategory
import com.bignerdranch.android.yelpsbusinessesweather.model.YelpLocation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
    @TypeConverter
    fun toList(title: String): List<YelpCategory> {
        val gson = Gson()
        val type = object : TypeToken<List<YelpCategory>>() {}.type
        return gson.fromJson<List<YelpCategory>>(title, type)
    }
    @TypeConverter
    fun fromList(title: List<YelpCategory>): String {
        val gson = Gson()
        val type = object : TypeToken<List<YelpCategory>>() {}.type
        return gson.toJson(title, type)
    }
    @TypeConverter
    fun toObject(title: String): YelpLocation {
        val yelpLocation = YelpLocation(title)
        return yelpLocation
    }
    @TypeConverter
    fun fromObject(title: YelpLocation): String {
        return title.address
    }
    @TypeConverter
    fun toObject1(latLng: String): Coordinates {
        val list = latLng.split(",")
        val lat = list[0].toDouble()
        val lon = list[1].toDouble()
        val coordinates = Coordinates(lat,lon)
        return coordinates
    }
    @TypeConverter
    fun fromObject1(title: Coordinates): String {
        return "${title.latitude},${title.longitude}"
    }
}