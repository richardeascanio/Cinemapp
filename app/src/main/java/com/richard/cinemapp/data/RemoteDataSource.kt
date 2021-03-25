package com.richard.cinemapp.data

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
        return moviesApi.getUpcoming(apiKey)
    }

}