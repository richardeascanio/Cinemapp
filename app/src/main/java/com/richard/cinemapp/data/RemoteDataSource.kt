package com.richard.cinemapp.data

import android.util.Log
import com.richard.cinemapp.data.network.MoviesApi
import com.richard.cinemapp.models.Result
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource
@Inject
constructor(
  private val moviesApi: MoviesApi
) {

    suspend fun getUpcomingMovies(queries: Map<String, String>): Response<Result> {
        Log.i("debug", "getUpcomingMovies: ${moviesApi.getUpcoming(queries)}")
        return moviesApi.getUpcoming(queries)
    }

    suspend fun getNowPlayingMovies(queries: Map<String, String>): Response<Result> {
        Log.i("debug", "getNowPlayingMovies: ${moviesApi.getNowPlaying(queries)}")
        return moviesApi.getNowPlaying(queries)
    }

    suspend fun getTopRatedMovies(queries: Map<String, String>): Response<Result> {
        Log.i("debug", "getTopRatedMovies: ${moviesApi.getTopRated(queries)}")
        return moviesApi.getTopRated(queries)
    }

}