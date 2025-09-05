package com.example.omdb.data.repository

import com.example.omdb.data.remote.NetworkModule
import com.example.omdb.data.remote.model.OmdbMovieResponse

class MovieRepository {
    private val api = NetworkModule.omdbApi

    // suspend: coroutines ile arka planda ağ isteği
    suspend fun getMovieByTitle(title: String): Result<OmdbMovieResponse> {
        return try {
            val resp = api.getMovieByTitle(title)
            // OMDb "Response" alanı "True" / "False"
            if (resp.response?.equals("True", ignoreCase = true) == true) {
                Result.success(resp)
            } else {
                Result.failure(IllegalArgumentException(resp.error ?: "Film bulunamadı"))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}