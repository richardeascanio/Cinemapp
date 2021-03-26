package com.richard.cinemapp.data.network

import com.richard.cinemapp.models.MovieResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviesApi {

    // Get upcoming movies
    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @QueryMap queries: Map<String, String>
    ): Response<MovieResult>

    // Get now playing movies
    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @QueryMap queries: Map<String, String>
    ): Response<MovieResult>

    // Get top rated movies
    @GET("movie/top_rated")
    suspend fun getTopRated(
        @QueryMap queries: Map<String, String>
    ): Response<MovieResult>

    // Get popular movies
    @GET("movie/popular")
    suspend fun getPopular(
        @QueryMap queries: Map<String, String>
    ): Response<MovieResult>

}