package com.richard.cinemapp.data.network

import com.richard.cinemapp.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String
    ): Response<Result>

}