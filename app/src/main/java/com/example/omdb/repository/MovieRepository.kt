package com.example.omdb.repository

import com.example.omdb.api.OmdbApiService
import com.example.omdb.model.SearchResponse
import javax.inject.Inject
import javax.inject.Singleton

// Basit repository: API çağrılarını barındırır.
// İleride caching (Room) eklemek istersen repository burada DB ile network'ü koordine eder.
@Singleton
class MovieRepository @Inject constructor(
    private val api: OmdbApiService
) {
    // Sadece network çağrısı yapıp SearchResponse döndürüyoruz.
    suspend fun searchMovies(query: String, page: Int = 1): SearchResponse =
        api.searchMovies(query, page)
}
