package com.example.omdb.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompletedRepository @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun addMovie(movie: MovieEntity) = dao.insertMovie(movie)
    suspend fun removeMovie(id: String) = dao.deleteMovie(id)

    fun getAlphabetical(): Flow<List<MovieEntity>> = dao.getMoviesAlphabetical()
    fun getByRating(): Flow<List<MovieEntity>> = dao.getMoviesByRating()
    fun getByUserRating(): Flow<List<MovieEntity>> = dao.getMoviesByUserRating()

    suspend fun getMovieDetail(id: String): MovieEntity? = dao.getMovieById(id)
}
