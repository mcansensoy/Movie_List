package com.example.omdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.model.MovieItem
import com.example.omdb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// HiltViewModel: Hilt ViewModel lifecyle desteği sağlar
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    // UI ile kolay bağlantı için StateFlow kullanıyorum
    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Arama fonksiyonu
    fun search(query: String, page: Int = 1) {
        if (query.isBlank()) {
            _error.value = "Lütfen bir kelime girin"
            return
        }
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val resp = repository.searchMovies(query.trim(), page)
                if (resp.response.equals("True", ignoreCase = true) && resp.search != null) {
                    _movies.value = resp.search
                } else {
                    _movies.value = emptyList()
                    _error.value = resp.error ?: "No results"
                }
            } catch (t: Throwable) {
                _movies.value = emptyList()
                _error.value = t.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}