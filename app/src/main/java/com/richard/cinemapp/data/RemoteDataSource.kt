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

    suspend fun getUpcomingMovies(apiKey: String): Response<Result> {
        Log.i("debug", "getUpcomingMovies: ${moviesApi.getUpcoming(apiKey)}")
        return moviesApi.getUpcoming(apiKey)
    }

}