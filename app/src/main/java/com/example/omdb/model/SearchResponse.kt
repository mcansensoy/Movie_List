package com.example.omdb.model

import com.google.gson.annotations.SerializedName

data class  SearchResponse(
    @SerializedName("Search") val search: List<MovieItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String, // "True"/"False"
    @SerializedName("Error") val error: String? = null
)

