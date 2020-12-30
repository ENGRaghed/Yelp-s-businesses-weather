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
    var id:String = "id",
    var name: String = "name",
    var rating: Double = 0.0,
    var price: String? ="price",
    @SerializedName("review_count") var numReviews: Int=0,
    @SerializedName("distance") var distanceInMeters: Double = 0.0,
    @SerializedName("image_url") var imageUrl: String ="imageUrl",
    var categories: List<YelpCategory>,
    var location: YelpLocation,
    var coordinates: Coordinates,
    var phone:String ="050") : Parcelable {
    fun displayDistance(): String {
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distanceInMeters * milesPerMeter)
        return "$distanceInMiles mi"
    }
}
@Parcelize
data class YelpCategory(
    var title: String ="title"
) : Parcelable

@Parcelize
data class YelpLocation(
    @SerializedName("address1") var address: String ="address"
) : Parcelable

@Parcelize
data class Coordinates(
    val latitude: Double = 0.0,
    val longitude: Double =0.0
) : Parcelable