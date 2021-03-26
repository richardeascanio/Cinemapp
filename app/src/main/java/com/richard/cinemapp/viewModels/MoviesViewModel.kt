package com.richard.cinemapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.richard.cinemapp.data.DataStoreRepository
import com.richard.cinemapp.data.Repository
import com.richard.cinemapp.models.Result
import com.richard.cinemapp.utils.Constants.API_KEY
import com.richard.cinemapp.utils.Constants.QUERY_API_KEY
import com.richard.cinemapp.utils.Constants.QUERY_REGION
import com.richard.cinemapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    // DATA STORE
    var networkStatus = false
    var backOnline = false
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    // RETROFIT
    var upcomingMoviesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()
    var nowPlayingMoviesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()
    var topRatedMoviesResponse: MutableLiveData<NetworkResult<Result>> = MutableLiveData()

    fun getUpcomingMovies(queries: Map<String, String>) = viewModelScope.launch {
        getUpcomingMoviesSafeCall(queries)
    }

    fun getNowPlayingMovies(queries: Map<String, String>) = viewModelScope.launch {
        getNowPlayingMoviesSafeCall(queries)
    }

    fun getTopRatedMovies(queries: Map<String, String>) = viewModelScope.launch {
        getTopRatedMoviesSafeCall(queries)
    }

    private suspend fun getUpcomingMoviesSafeCall(queries: Map<String, String>) {
        upcomingMoviesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getUpcomingMovies(queries)
                upcomingMoviesResponse.postValue(handleUpcomingMoviesResponse(response))
            } catch (e: Exception) {
                upcomingMoviesResponse.postValue(NetworkResult.Error("Movies not found."))
            }
        }
    }

    private suspend fun getNowPlayingMoviesSafeCall(queries: Map<String, String>) {
        nowPlayingMoviesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getNowPlayingMovies(queries)
                nowPlayingMoviesResponse.postValue(handleNowPlayingMoviesResponse(response))
            } catch (e: Exception) {
                nowPlayingMoviesResponse.postValue(NetworkResult.Error("Movies not found."))
            }
        }
    }

    private suspend fun getTopRatedMoviesSafeCall(queries: Map<String, String>) {
        topRatedMoviesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getTopRatedMovies(queries)
                topRatedMoviesResponse.postValue(handleTopRatedMoviesResponse(response))
            } catch (e: Exception) {
                topRatedMoviesResponse.postValue(NetworkResult.Error("Movies not found."))
            }
        }
    }

    private fun handleUpcomingMoviesResponse(response: Response<Result>): NetworkResult<Result> {
        return when {
            response.body()!!.movies.isNullOrEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful -> {
                val upcomingMovies = response.body()
                NetworkResult.Success(upcomingMovies!!)
            } else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleNowPlayingMoviesResponse(response: Response<Result>): NetworkResult<Result> {
        return when {
            response.body()!!.movies.isNullOrEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful -> {
                val nowPlayingMovies = response.body()
                NetworkResult.Success(nowPlayingMovies!!)
            } else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleTopRatedMoviesResponse(response: Response<Result>): NetworkResult<Result> {
        return when {
            response.body()!!.movies.isNullOrEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful -> {
                val topRatedMovies = response.body()
                NetworkResult.Success(topRatedMovies!!)
            } else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    fun applyQueries(region: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_REGION] = region

        return queries
    }

    private fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No internet connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(false)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(true)
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