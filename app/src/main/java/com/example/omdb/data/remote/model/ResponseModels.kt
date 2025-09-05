package com.example.omdb.data.remote.model

import com.google.gson.annotations.SerializedName

// API’den gelen JSON’un listeli response modeli
// Örnek JSON: { "Search": [ { "Title": "...", "Year": "...", ... } ], "totalResults": "321", "Response": "True" }
data class SearchResponse(
    @SerializedName("Search") val search: List<MovieItem>?, // Film listesi (null olabilir)
    @SerializedName("totalResults") val totalResults: String?, // toplam sonuç sayısı
    @SerializedName("Response") val response: String          // "True" ya da "False"
)

// Tek bir film item’ı
data class MovieItem(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Poster") val poster: String // Poster URL (ya da "N/A")
)
