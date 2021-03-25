package com.richard.cinemapp.models

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("results")
    val results: List<Movie>
)