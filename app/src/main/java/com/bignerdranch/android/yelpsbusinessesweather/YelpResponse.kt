package com.bignerdranch.android.yelpsbusinessesweather

import com.google.gson.annotations.SerializedName

class YelpResponse {
    @SerializedName("businesses")
    lateinit var businesses: List<YelpRestaurant>
}