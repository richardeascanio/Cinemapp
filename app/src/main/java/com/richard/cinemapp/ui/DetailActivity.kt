package com.richard.cinemapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.navArgs
import coil.load
import com.richard.cinemapp.R
import com.richard.cinemapp.databinding.ActivityDetailBinding
import com.richard.cinemapp.utils.Constants
import com.richard.cinemapp.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val args by navArgs<DetailActivityArgs>()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("debug", "DetailsActivity: movie ${args.movie}")
        Log.d("debug", "DetailsActivity: series ${args.series}")

        fillData()
    }

    private fun fillData() {
        args.movie?.let { movie ->
            binding.apply {
                movie.posterPath?.let {
                    val url = Constants.BASE_IMAGE_URL + it
                    posterImageView.load(url) {
                        crossfade(true)
                        crossfade(600)
                        error(R.drawable.ic_error_placeholder)
                    }
                }
                titleTextView.text = movie.title
                ratingTextView.text = movie.voteAverage.toString()
                overviewTextView.text = movie.overview
            }
            binding.rateButton.setOnClickListener {
                Log.d("debug", "rateButton: id ${movie.id}")
                Log.d("debug", "rateButton: rating ${binding.rateRatingBar.rating}")
                viewModel.rateMovie(
                        movie.id,
                        viewModel.applyQueries(),
                        binding.rateRatingBar.rating.toString()
                )
            }
        }
    }
}