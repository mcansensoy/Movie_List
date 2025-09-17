package com.example.omdb.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor(
    private val dao: WatchlistDao
) {
    suspend fun addMovie(movie: WatchlistEntity) = dao.insertMovie(movie)
    suspend fun removeMovie(id: String) = dao.deleteMovie(id)

    fun getAlphabetical(): Flow<List<WatchlistEntity>> = dao.getMoviesAlphabetical()
    fun getByRating(): Flow<List<WatchlistEntity>> = dao.getMoviesByRating()

    suspend fun getMovieDetail(id: String): WatchlistEntity? = dao.getMovieById(id)
}
