package com.example.omdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.CompletedRepository
import com.example.omdb.data.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortType { ALPHABETICAL, RATING, USER_RATING }

@HiltViewModel
class CompletedViewModel @Inject constructor(
    private val repo: CompletedRepository
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.ALPHABETICAL)
    val sortType: StateFlow<SortType> = _sortType

    val movies: StateFlow<List<MovieEntity>> = _sortType.flatMapLatest { type ->
        when (type) {
            SortType.ALPHABETICAL -> repo.getAlphabetical()
            SortType.RATING -> repo.getByRating()
            SortType.USER_RATING -> repo.getByUserRating()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun changeSort(type: SortType) {
        _sortType.value = type
    }

    fun addMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repo.addMovie(movie)
        }
    }

    fun removeMovie(id: String) {
        viewModelScope.launch {
            repo.removeMovie(id)
        }
    }
}
