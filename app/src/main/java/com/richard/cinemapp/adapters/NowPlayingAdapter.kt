package com.richard.cinemapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.richard.cinemapp.databinding.NowPlayingRowLayoutBinding
import com.richard.cinemapp.models.Movie
import com.richard.cinemapp.utils.MovieDiffUtil

class NowPlayingAdapter : RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder>(){

    private var nowPlayingList = emptyList<Movie>()

    class NowPlayingViewHolder(
        private val binding: NowPlayingRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup): NowPlayingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NowPlayingRowLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return NowPlayingViewHolder(binding)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        return NowPlayingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        val currentMovie = nowPlayingList[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return nowPlayingList.size
    }

    fun setData(newData: List<Movie>) {
        val movieDiffUtil = MovieDiffUtil(nowPlayingList, newData)
        val diffUtilMovies = DiffUtil.calculateDiff(movieDiffUtil)
        nowPlayingList = newData
        diffUtilMovies.dispatchUpdatesTo(this)
    }

}