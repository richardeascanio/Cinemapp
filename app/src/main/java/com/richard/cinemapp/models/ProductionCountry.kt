package com.richard.cinemapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso: String,
    @SerializedName("name")
    val name: String
): Parcelable