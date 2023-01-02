package com.creativijaya.moviegallery.presentation.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.domain.usecases.DiscoverMovieUseCase
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.utils.Fail
import com.creativijaya.moviegallery.utils.Success
import com.creativijaya.moviegallery.utils.orZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val discoverMovieUseCase: DiscoverMovieUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedGenre = savedStateHandle.getStateFlow<GenreDto?>(
        KEY_SELECTED_GENRE, null
    )

    private val _currentPage = savedStateHandle.getStateFlow(
        KEY_CURRENT_PAGE, 1
    )

    private val _isLoading = MutableStateFlow(false)

    private val _movieListAsync = combine(
        selectedGenre, _currentPage
    ) { genre, page ->
        _isLoading.value = false
        discoverMovieUseCase(
            page = page,
            genreIds = if (genre == null) emptyList() else listOf(genre.id)
        )
    }

    private val movieList = arrayListOf<MovieDto?>()

    val uiState: StateFlow<HomeUiState> = combine(
        _isLoading, _currentPage, _movieListAsync
    ) { isLoading, page, async ->
        if (isLoading) {
            if (page <= 1) {
                HomeUiState(
                    isLoading = true,
                    movieList = emptyList()
                )
            } else {
                HomeUiState(
                    movieList = movieList + listOf(null)
                )
            }
        } else {
            when (async) {
                is Success -> {
                    savedStateHandle[KEY_TOTAL_PAGE] = async.invoke().totalPages

                    if (page <= 1) {
                        movieList.remove(null)
                    }

                    movieList.addAll(async.invoke().results)
                    HomeUiState(
                        isLoading = false,
                        movieList = movieList
                    )
                }
                is Fail -> HomeUiState(
                    isLoading = false,
                    error = async.error
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(
            isLoading = true
        )
    )

    fun onLoadMore() {
        val currentPage = _currentPage.value
        val totalPages = savedStateHandle.get<Int>(KEY_TOTAL_PAGE).orZero()

        if (_isLoading.value || currentPage >= totalPages) return

        _isLoading.value = true
        savedStateHandle[KEY_CURRENT_PAGE] = currentPage + 1

        Log.d("DEBUG_MAIN", "onLoadMore ${currentPage + 1}")
    }

    fun handleOnFilterApplied(genre: GenreDto) {
        savedStateHandle[KEY_SELECTED_GENRE] = genre.id
        savedStateHandle[KEY_CURRENT_PAGE] = 1
//        _uiState.update {
//            it.resetMovieList().copy(
//                selectedGenre = genre
//            )
//        }
//
//        handleOnDiscoverMovieList()
    }

    companion object {
        private const val KEY_SELECTED_GENRE = "HomeViewModel.KEY_SELECTED_GENRE"
        private const val KEY_CURRENT_PAGE = "HomeViewModel.KEY_CURRENT_PAGE"
        private const val KEY_TOTAL_PAGE = "HomeViewModel.KEY_TOTAL_PAGE"
    }

}
