package com.richard.cinemapp.bindingAdapters

import android.util.Log
import android.widget.RatingBar
import androidx.databinding.BindingAdapter

class NowPlayingRowLayoutBinding {

    companion object {

        @BindingAdapter("setRatingStars")
        @JvmStatic
        fun setRatingStars(ratingBar: RatingBar, rating: Double) {
            val stars: Float = (rating/2).toFloat()
            Log.d("debug", "setRatingStars: stars $stars")
            ratingBar.rating = stars
        }

    }

}