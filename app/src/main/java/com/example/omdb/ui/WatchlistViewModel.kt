package com.example.omdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.data.WatchlistEntity
import com.example.omdb.data.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class WatchlistSortType { ALPHABETICAL, RATING }

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repo: WatchlistRepository
) : ViewModel() {

    private val _sortType = MutableStateFlow(WatchlistSortType.ALPHABETICAL)
    val sortType: StateFlow<WatchlistSortType> = _sortType

    val movies: StateFlow<List<WatchlistEntity>> = _sortType.flatMapLatest { type ->
        when (type) {
            WatchlistSortType.ALPHABETICAL -> repo.getAlphabetical()
            WatchlistSortType.RATING -> repo.getByRating()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun changeSort(type: WatchlistSortType) {
        _sortType.value = type
    }

    fun addMovie(movie: WatchlistEntity) {
        viewModelScope.launch { repo.addMovie(movie) }
    }

    fun removeMovie(id: String) {
        viewModelScope.launch { repo.removeMovie(id) }
    }
}
