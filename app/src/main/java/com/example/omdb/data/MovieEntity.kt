package com.example.omdb.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omdb.model.MovieDetail
import com.example.omdb.model.MovieItem

@Entity(tableName = "completed_movies")
data class MovieEntity(
    @PrimaryKey val imdbID: String,
    val title: String,
    val year: String,
    val poster: String?,
    val imdbRating: String?,
    val genre: String?,
    val director: String?,
    val plot: String?,
    val userRating: Int? = null
){
    companion object {
        fun fromDetail(detail: MovieDetail, item: MovieItem): MovieEntity {
            return MovieEntity(
                imdbID = item.imdbID,
                title = detail.title ?: "Unknown",
                year = detail.year ?: "Unknown",
                poster = item.poster,
                imdbRating = detail.imdbRating,
                genre = detail.genre,
                director = detail.director,
                plot = detail.plot
            )
        }
    }
}



