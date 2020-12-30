package com.bignerdranch.android.yelpsbusinessesweather.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "day_plan_table")
data class DayPlan(
        @PrimaryKey(autoGenerate = true)
        var dayPlanId : Int = 0,
        val name: String,
        val rating: Double,
        val price: String?,
        @SerializedName("review_count") val numReviews: Int,
        @SerializedName("distance") val distanceInMeters: Double,
        @SerializedName("image_url") val imageUrl: String,
        val categories: List<YelpCategory>,
        val location: YelpLocation,
        val coordinates: Coordinates,
        var note : String ="",
        var date: Date? = null,
        var state : Boolean = false
) : Parcelable