package com.richard.cinemapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.richard.cinemapp.databinding.PopularSeriesRowLayoutBinding
import com.richard.cinemapp.models.Series
import com.richard.cinemapp.utils.MovieDiffUtil

class PopularSeriesAdapter : RecyclerView.Adapter<PopularSeriesAdapter.PopularViewHolder>() {

    private var popularList = emptyList<Series>()

    class PopularViewHolder(
        private val binding: PopularSeriesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(series: Series) {
            binding.series = series
            binding.executePendingBindings()
        }

        companion object {

            fun from(parent: ViewGroup) : PopularViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PopularSeriesRowLayoutBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return PopularViewHolder(binding)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val currentSeries = popularList[position]
        holder.bind(currentSeries)
    }

    override fun getItemCount(): Int {
        return popularList.size
    }

    fun setData(newData: List<Series>) {
        val seriesDiffUtil = MovieDiffUtil(popularList, newData)
        val diffUtilSeries = DiffUtil.calculateDiff(seriesDiffUtil)
        popularList = newData
        diffUtilSeries.dispatchUpdatesTo(this)
    }

}