package com.example.omdb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb.model.MovieDetail
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

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _page = MutableStateFlow(1)
    val page: StateFlow<Int> = _page

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun goToPage(newPage: Int) {
        _page.value = newPage
        search(_query.value, newPage)
    }

    private val _movieDetailList = MutableStateFlow<Map<String, MovieDetail>>(emptyMap()) //bunu ekledim
    val movieDetailList: StateFlow<Map<String, MovieDetail>> = _movieDetailList

    private val _movieDetails = MutableStateFlow<Map<String, MovieDetail>>(emptyMap())
    val movieDetails: StateFlow<Map<String, MovieDetail>> = _movieDetails

    fun toggleMovieDetail(imdbId: String) {
        // Eğer detay zaten gösteriliyorsa → kaldır
        if (_movieDetails.value.containsKey(imdbId)) {
            _movieDetails.value = _movieDetails.value - imdbId
        } else {
            // movieDetailList'te varsa oradan al
            _movieDetailList.value[imdbId]?.let { detail ->
                _movieDetails.value = _movieDetails.value + (imdbId to detail)
            }
        }
    }


    // Arama ve filtreleme işlemleri
    private val _yearRange = MutableStateFlow(1900..2100)
    val yearRange: StateFlow<IntRange> = _yearRange

    private val _ratingRange = MutableStateFlow(0f..10f)
    val ratingRange: StateFlow<ClosedFloatingPointRange<Float>> = _ratingRange

    fun updateYearRange(start: Int, end: Int) {
        _yearRange.value = start..end
        applyFilters()
    }

    fun updateRatingRange(start: Float, end: Float) {
        _ratingRange.value = start..end
        applyFilters()
    }

    private var lastRawMovies: List<MovieItem> = emptyList() // filtresiz gelen listeyi tut

    // Arama fonksiyonu
    fun search(query: String, page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.searchMovies(query, page)
                lastRawMovies = response.search ?: emptyList()

                // Önce movieDetailList'i temizle
                _movieDetailList.value = emptyMap()

                // Her film için detay çek ve map'e ekle
                lastRawMovies.forEach { movie ->
                    launch {
                        try {
                            val detail = repository.getMovieDetail(movie.imdbID)
                            _movieDetailList.value = _movieDetailList.value + (movie.imdbID to detail)
                            applyFilters() // her yeni detail geldikçe filtreyi tekrar uygula
                        } catch (e: Exception) {
                            // hata olursa Unknown detail ekleyelim
                            _movieDetailList.value = _movieDetailList.value + (movie.imdbID to MovieDetail(
                                title = null,
                                year = null,
                                imdbRating = "Unknown",
                                genre = "Unknown",
                                director = "Unknown",
                                plot = "Unknown",
                                response = "False"
                            ))
                        }
                    }
                }

                applyFilters()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun applyFilters() {
        val filtered = lastRawMovies.filter { movie ->
            val yearOk = movie.year.toIntOrNull()?.let {
                it in _yearRange.value
            } ?: true

            val detail = _movieDetailList.value[movie.imdbID]
            val ratingOk = detail?.imdbRating?.toFloatOrNull()?.let {
                it in _ratingRange.value
            } ?: true

            yearOk && ratingOk
        }
        _movies.value = filtered
    }

    fun applyFiltersWithValues(startYear: String, endYear: String, minRating: String, maxRating: String): Boolean {
        return try {
            val start = startYear.toInt()
            val end = endYear.toInt()
            val min = minRating.toFloat()
            val max = maxRating.toFloat()

            _yearRange.value = start..end
            _ratingRange.value = min..max
            applyFilters()
            true // başarılı
        } catch (e: Exception) {
            false // sayı dışında bir şey girilmiş
        }
    }


}