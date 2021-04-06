package com.richard.cinemapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.richard.cinemapp.data.Repository
import com.richard.cinemapp.models.MovieResult
import com.richard.cinemapp.models.RateResult
import com.richard.cinemapp.utils.Constants.API_KEY
import com.richard.cinemapp.utils.Constants.QUERY_API_KEY
import com.richard.cinemapp.utils.Constants.QUERY_SESSION_ID
import com.richard.cinemapp.utils.Constants.SESSION_ID
import com.richard.cinemapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    fun rateMovie(movieId: Int, queries: Map<String, String>, value: String) = viewModelScope.launch {
        rateMovieSafeCall(movieId, queries, value)
    }

    private suspend fun rateMovieSafeCall(movieId: Int, queries: Map<String, String>, value: String) {
        if (hasInternetConnection()) {
            val response = repository.remote.rateMovie(movieId, queries, value)
            Toast.makeText(getApplication(), handleRateMovieResponse(response), Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleRateMovieResponse(response: Response<RateResult>): String {
        return when {
            response.isSuccessful -> {
                val rateResult = response.body()
                rateResult!!.statusMessage
            } else -> {
                response.message()
            }
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_SESSION_ID] = SESSION_ID

        return queries
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