package com.bignerdranch.android.yelpsbusinessesweather.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "businesses_table")
@Parcelize
data class YelpRestaurant(
    @PrimaryKey(autoGenerate = true)
    var yelpId : Int = 0,
    val name: String,
    val rating: Double,
    val price: String?,
    @SerializedName("review_count") val numReviews: Int,
    @SerializedName("distance") val distanceInMeters: Double,
    @SerializedName("image_url") val imageUrl: String,
    val categories: List<YelpCategory>,
    val location: YelpLocation,
    val coordinates: Coordinates
) : Parcelable {
    fun displayDistance(): String {
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles mi"
    }
}
@Parcelize
data class YelpCategory(
    val title: String
) : Parcelable

@Parcelize
data class YelpLocation(
    @SerializedName("address1") val address: String
) : Parcelable

@Parcelize
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) : Parcelable