package com.example.omdb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_movies")
data class WatchlistEntity(
    @PrimaryKey val imdbID: String,
    val title: String,
    val year: String,
    val poster: String?,
    val imdbRating: String?,
    val genre: String?,
    val director: String?,
    val plot: String?
)
