package com.bignerdranch.android.yelpsbusinessesweather.network

import com.bignerdranch.android.yelpsbusinessesweather.model.YelpRestaurant
import com.google.gson.annotations.SerializedName

class YelpResponse {
    @SerializedName("businesses")
    lateinit var businesses: List<YelpRestaurant>
}