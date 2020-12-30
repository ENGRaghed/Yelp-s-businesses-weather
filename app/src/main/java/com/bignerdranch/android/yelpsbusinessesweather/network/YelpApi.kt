package com.bignerdranch.android.yelpsbusinessesweather.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface YelpApi {
    @GET("businesses/search")
    suspend fun searchRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("latitude") lat : String,
        @Query("longitude") lon : String) : YelpResponse


    @GET("businesses/search")
    suspend fun searchBusinessesByCategory(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("latitude") lat : String,
        @Query("longitude") lon : String) : YelpResponse

    @GET("businesses/{id}")
    suspend fun BusinessesDetails(
            @Path("id") id : String,
            @Header("Authorization") authHeader: String) : PhotosResponse
}