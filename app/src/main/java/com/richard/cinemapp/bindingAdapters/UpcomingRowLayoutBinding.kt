package com.richard.cinemapp.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.richard.cinemapp.R
import com.richard.cinemapp.utils.Constants.BASE_IMAGE_URL

class UpcomingRowLayoutBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, posterUrl: String) {
            val url = BASE_IMAGE_URL + posterUrl
            imageView.load(url) {
                crossfade(true)
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
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

    }

}