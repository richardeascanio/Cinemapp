package com.richard.cinemapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import coil.load
import com.richard.cinemapp.R
import com.richard.cinemapp.databinding.ActivityDetailBinding
import com.richard.cinemapp.models.Movie
import com.richard.cinemapp.utils.Constants
import com.richard.cinemapp.utils.Constants.API_KEY
import com.richard.cinemapp.utils.Constants.BASE_IMAGE_URL
import com.richard.cinemapp.utils.NetworkListener
import com.richard.cinemapp.utils.NetworkResult
import com.richard.cinemapp.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val args by navArgs<DetailActivityArgs>()
    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("debug", "DetailsActivity: movie ${args.movie}")
        Log.d("debug", "DetailsActivity: series ${args.series}")

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(applicationContext)
                .collect {
                    getMovieDetails()
                }
        }

    }

    private fun fillData(currentMovie: Movie) {
        currentMovie.let { movie ->
            Log.d("debug", "fillData: movie $movie")
            binding.apply {
                movie.posterPath?.let {
                    val url = BASE_IMAGE_URL + it
                    posterImageView.load(url) {
                        crossfade(true)
                        crossfade(600)
                        error(R.drawable.ic_error_placeholder)
                    }
                }
                titleTextView.text = movie.title
                ratingTextView.text = movie.voteAverage.toString()
                overviewTextView.text = movie.overview
                rateButton.setOnClickListener {
                    Log.d("debug", "rateButton: id ${movie.id}")
                    Log.d("debug", "rateButton: rating ${rateRatingBar.rating}")
                    viewModel.rateMovie(
                            movie.id,
                            viewModel.applyQueries(),
                            rateRatingBar.rating.toString()
                    )
                }
            }
        }
    }

    private fun getMovieDetails() {
        val lifecycleOwner: LifecycleOwner = this
        lifecycleScope.launch {
            viewModel.getMovieDetail(args.movie!!.id, API_KEY)
            viewModel.movieDetailResponse.observe(lifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            fillData(it)
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                                applicationContext,
                                response.message.toString(),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            }
        }
    }
}