package com.richard.cinemapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.richard.cinemapp.databinding.UpcomingRowLayoutBinding
import com.richard.cinemapp.models.Movie
import com.richard.cinemapp.utils.MovieDiffUtil

class UpcomingAdapter : RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>() {

    private var upcomingList = emptyList<Movie>()

    class UpcomingViewHolder(
        private val binding: UpcomingRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup) : UpcomingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UpcomingRowLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return UpcomingViewHolder(binding)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        return UpcomingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val currentMovie = upcomingList[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return upcomingList.size
    }

    fun setData(newData: List<Movie>) {
        val movieDiffUtil = MovieDiffUtil(upcomingList, newData)
        val diffUtilMovies = DiffUtil.calculateDiff(movieDiffUtil)
        upcomingList = newData
        diffUtilMovies.dispatchUpdatesTo(this)
    }

}