package com.example.omdb.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM completed_movies WHERE imdbID = :id")
    suspend fun deleteMovie(id: String)

    @Query("SELECT * FROM completed_movies ORDER BY title ASC")
    fun getMoviesAlphabetical(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM completed_movies ORDER BY CAST(imdbRating AS REAL) DESC")
    fun getMoviesByRating(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM completed_movies ORDER BY userRating DESC")
    fun getMoviesByUserRating(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM completed_movies WHERE imdbID = :id LIMIT 1")
    suspend fun getMovieById(id: String): MovieEntity?
}
