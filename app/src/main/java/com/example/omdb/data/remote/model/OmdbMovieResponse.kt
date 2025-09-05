package com.example.omdb.data.remote.model

import com.squareup.moshi.Json

// OMDb, alan adlarını büyük harfle döndürür (Title, imdbRating, Response, Error).
// Moshi’de @Json ile JSON anahtarını Kotlin property’sine eşliyoruz.
data class OmdbMovieResponse(
    @Json(name = "Title") val title: String?,
    @Json(name = "imdbRating") val imdbRating: String?,
    @Json(name = "Response") val response: String?, // "True"/"False"
    @Json(name = "Error") val error: String?
)
