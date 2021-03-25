package com.richard.cinemapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.richard.cinemapp.data.Repository
import com.richard.cinemapp.models.Result
import com.richard.cinemapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    // RETROFIT
    var upcomingMoviesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()

    fun getUpcomingMovies(apiKey: String) = viewModelScope.launch {
        getUpcomingMoviesSafeCall(apiKey)
    }

    private suspend fun getUpcomingMoviesSafeCall(apiKey: String) {
        upcomingMoviesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getUpcomingMovies(apiKey)
                upcomingMoviesResponse.postValue(handleUpcomingMoviesResponse(response))
                val upcomingMovies = upcomingMoviesResponse.value!!.data
            } catch (e: Exception) {
                upcomingMoviesResponse.postValue(NetworkResult.Error("Movies not found."))
            }
        } else {
            upcomingMoviesResponse.postValue(NetworkResult.Error("No Internet Connection."))
        }
    }

    private fun handleUpcomingMoviesResponse(response: Response<Result>): NetworkResult<Result> {
        when {
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful -> {
                val upcomingMovies = response.body()
                return NetworkResult.Success(upcomingMovies!!)
            } else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}