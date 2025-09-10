package com.example.omdb.api

import com.example.omdb.model.MovieDetail
import com.example.omdb.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    // s: search string; page: pagination (1..100)
    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("page") page: Int = 1
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetail(
        @Query("i") imdbId: String
    ): MovieDetail
}