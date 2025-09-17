package com.example.omdb.repository

import com.example.omdb.api.OmdbApiService
import com.example.omdb.model.SearchResponse
import javax.inject.Inject
import javax.inject.Singleton

// Basit repository: API çağrılarını barındırıyo
@Singleton
class MovieRepository @Inject constructor(
    private val api: OmdbApiService
) {
    suspend fun searchMovies(query: String, page: Int = 1): SearchResponse =
        api.searchMovies(query, page)

    suspend fun getMovieDetail(imdbId: String) = api.getMovieDetail(imdbId)
}
