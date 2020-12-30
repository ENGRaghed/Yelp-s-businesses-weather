package com.bignerdranch.android.yelpsbusinessesweather.network

import com.google.gson.annotations.SerializedName

class PhotosResponse{
    @SerializedName("photos")
    lateinit var photos: List<String>
}