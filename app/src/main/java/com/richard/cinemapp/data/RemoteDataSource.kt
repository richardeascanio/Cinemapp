package com.richard.cinemapp.data

import android.util.Log
import com.richard.cinemapp.data.network.MoviesApi
import com.richard.cinemapp.data.network.SeriesApi
import com.richard.cinemapp.models.MovieResult
import com.richard.cinemapp.models.RateResult
import com.richard.cinemapp.models.SeriesResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource
@Inject
constructor(
  private val moviesApi: MoviesApi,
  private val seriesApi: SeriesApi
) {

    // Movies
    suspend fun getUpcomingMovies(queries: Map<String, String>): Response<MovieResult> {
        Log.d("debug", "getUpcomingMovies: ${moviesApi.getUpcoming(queries)}")
        return moviesApi.getUpcoming(queries)
    }

    suspend fun getNowPlayingMovies(queries: Map<String, String>): Response<MovieResult> {
        Log.d("debug", "getNowPlayingMovies: ${moviesApi.getNowPlaying(queries)}")
        return moviesApi.getNowPlaying(queries)
    }

    suspend fun getTopRatedMovies(queries: Map<String, String>): Response<MovieResult> {
        Log.d("debug", "getTopRatedMovies: ${moviesApi.getTopRated(queries)}")
        return moviesApi.getTopRated(queries)
    }

    suspend fun getPopularMovies(queries: Map<String, String>): Response<MovieResult> {
        Log.d("debug", "getPopularMovies: ${moviesApi.getPopular(queries)}")
        return moviesApi.getPopular(queries)
    }

    suspend fun rateMovie(movieId: Int, queries: Map<String, String>, value: String): Response<RateResult> {
        Log.d("debug", "rateMovie: ${moviesApi.postRating(movieId, queries, value)}")
        return moviesApi.postRating(movieId, queries, value)
    }

    // Series
    suspend fun getPopularSeries(apiKey: String): Response<SeriesResult> {
        Log.d("debug", "getPopularSeries: ${seriesApi.getPopular(apiKey)}")
        return seriesApi.getPopular(apiKey)
    }

}