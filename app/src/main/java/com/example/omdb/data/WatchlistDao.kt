package com.example.omdb.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: WatchlistEntity)

    @Query("DELETE FROM watchlist_movies WHERE imdbID = :id")
    suspend fun deleteMovie(id: String)

    @Query("SELECT * FROM watchlist_movies ORDER BY title ASC")
    fun getMoviesAlphabetical(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_movies ORDER BY CAST(imdbRating AS REAL) DESC")
    fun getMoviesByRating(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist_movies WHERE imdbID = :id LIMIT 1")
    suspend fun getMovieById(id: String): WatchlistEntity?
}
