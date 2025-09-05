package com.example.omdb.data.remote

import com.example.omdb.data.remote.model.OmdbMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

// OMDb baseUrl: https://www.omdbapi.com/
// Tek film getirmek için: ?t=Title  (örn. ?t=Inception)
// Dönen JSON’daki "imdbRating" ve "Title" alanlarını kullanacağız.
interface OmdbApi {
    @GET(".") // baseUrl + "." = kök path; parametreler @Query ile eklenecek
    suspend fun getMovieByTitle(
        @Query("t") title: String,
        @Query("plot") plot: String = "short",
        @Query("r") responseFormat: String = "json"
    ): OmdbMovieResponse
}