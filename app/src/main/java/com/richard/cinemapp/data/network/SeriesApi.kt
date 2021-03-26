package com.richard.cinemapp.data.network

import com.richard.cinemapp.models.SeriesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesApi {

    // Get popular series
    @GET("tv/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String
    ): Response<SeriesResult>

}