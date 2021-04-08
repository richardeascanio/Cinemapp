package com.richard.cinemapp.data.network

import com.richard.cinemapp.models.Movie
import com.richard.cinemapp.models.MovieResult
import com.richard.cinemapp.models.RateResult
import retrofit2.Response
import retrofit2.http.*

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

    // Get movie details
    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") id: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie>

    // Rate movie
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("movie/{movieId}/rating")
    suspend fun postRating(
        @Path("movieId") id: Int,
        @QueryMap queries: Map<String, String>,
        @Field("value") value: String
    ): Response<RateResult>

}