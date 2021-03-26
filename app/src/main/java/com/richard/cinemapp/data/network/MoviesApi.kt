package com.richard.cinemapp.data.network

import com.richard.cinemapp.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviesApi {

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @QueryMap queries: Map<String, String>
    ): Response<Result>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @QueryMap queries: Map<String, String>
    ): Response<Result>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @QueryMap queries: Map<String, String>
    ): Response<Result>

    @GET("movie/popular")
    suspend fun getPopular(
        @QueryMap queries: Map<String, String>
    ): Response<Result>

}