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
import com.richard.cinemapp.models.SeriesResult
import com.richard.cinemapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel
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
    var popularSeriesResponse: MutableLiveData<NetworkResult<SeriesResult>> = MutableLiveData()

    fun getPopularSeries(apiKey: String) = viewModelScope.launch {
        getPopularSeriesSafeCall(apiKey)
    }

    private suspend fun getPopularSeriesSafeCall(apiKey: String) {
        popularSeriesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getPopularSeries(apiKey)
                popularSeriesResponse.postValue(handlePopularSeriesResponse(response))
            } catch (e: Exception) {
                popularSeriesResponse.postValue(NetworkResult.Error("Movies not found."))
            }
        }
    }

    private fun handlePopularSeriesResponse(response: Response<SeriesResult>): NetworkResult<SeriesResult> {
        return when {
            response.body()!!.series.isNullOrEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }
            response.isSuccessful -> {
                val popularSeries = response.body()
                NetworkResult.Success(popularSeries!!)
            } else -> {
                NetworkResult.Error(response.message())
            }
        }
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