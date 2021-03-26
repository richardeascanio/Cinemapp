package com.richard.cinemapp.models

import com.google.gson.annotations.SerializedName

data class SeriesResult(
    @SerializedName("results")
    val series: List<Series>
)