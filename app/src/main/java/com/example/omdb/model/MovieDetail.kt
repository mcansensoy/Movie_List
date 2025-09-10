package com.example.omdb.model

import com.google.gson.annotations.SerializedName

// OMDb 'i=' request response
data class MovieDetail(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("Genre") val genre: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String? = null
)
