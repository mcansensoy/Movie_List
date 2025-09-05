package com.example.omdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.remote.model.OmdbMovieResponse
import com.example.omdb.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// UI'nin kullanacağı sade durum modeli:
sealed class MovieUiState {
    data object Idle : MovieUiState()                // ilk ekran
    data object Loading : MovieUiState()             // arama sırasında
    data class Success(val data: OmdbMovieResponse) : MovieUiState()
    data class Error(val message: String) : MovieUiState()
}

class MovieViewModel : ViewModel() {

    private val repo = MovieRepository()

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Idle)
    val uiState: StateFlow<MovieUiState> = _uiState

    fun search(title: String) {
        if (title.isBlank()) {
            _uiState.value = MovieUiState.Error("Lütfen bir film adı girin")
            return
        }
        _uiState.value = MovieUiState.Loading

        // viewModelScope: lifecycle'a bağlı coroutine scope  :contentReference[oaicite:19]{index=19}
        viewModelScope.launch {
            val result = repo.getMovieByTitle(title.trim())
            _uiState.value = result.fold(
                onSuccess = { MovieUiState.Success(it) },
                onFailure = { MovieUiState.Error(it.message ?: "Bilinmeyen hata") }
            )
        }
    }
}