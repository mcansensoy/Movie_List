package com.example.omdb.api

import com.example.omdb.model.MovieDetail
import com.example.omdb.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
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