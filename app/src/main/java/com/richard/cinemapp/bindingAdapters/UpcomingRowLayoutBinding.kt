package com.richard.cinemapp.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.richard.cinemapp.R
import com.richard.cinemapp.models.Movie
import com.richard.cinemapp.ui.fragments.movies.MoviesFragmentDirections
import com.richard.cinemapp.utils.Constants.BASE_IMAGE_URL

class UpcomingRowLayoutBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, posterUrl: String?) {
            posterUrl?.let {
                val url = BASE_IMAGE_URL + it
                imageView.load(url) {
                    crossfade(true)
                    crossfade(600)
                    error(R.drawable.ic_error_placeholder)
                }
            }
        }

        @BindingAdapter("formattedDate")
        @JvmStatic
        fun formattedDate(textView: TextView, date: String) {
            val formattedDate = if (date.contains("-"))
                date.replace("-", "/")
            else
                date
            textView.text = formattedDate
        }

        @BindingAdapter("onMovieClicked")
        @JvmStatic
        fun onMovieClicked(movieRowLayout: ConstraintLayout, movie: Movie) {
            movieRowLayout.setOnClickListener {
                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailActivity(movie)
                movieRowLayout.findNavController().navigate(action)
            }
        }

    }

}