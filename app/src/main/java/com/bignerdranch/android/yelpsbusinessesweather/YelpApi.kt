package com.bignerdranch.android.yelpsbusinessesweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApi {
    @GET("businesses/search")
    suspend fun searchRestaurants(
        @Header("Authorization") authHeader: String,
//        @Query("term") searchTerm: String,
        @Query("latitude") lat : String,
        @Query("longitude") lon : String) : YelpResponse
//    ,@Query("radius") radius :String
}