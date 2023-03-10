package com.creativijaya.moviegallery.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.usecases.DiscoverMovieUseCase
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val discoverMovieUseCase: DiscoverMovieUseCase
) : ViewModel() {

    sealed class Event {
        object OnGetGenreList : Event()
        object OnDiscoverMovieList : Event()
        object OnMovieListAdded : Event()
        data class OnFilterApplied(val genre: GenreDto) : Event()
    }

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState>
        get() = _uiState
            .asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            Event.OnGetGenreList -> handleOnGetGenreList()
            Event.OnDiscoverMovieList -> handleOnDiscoverMovieList()
            Event.OnMovieListAdded -> handleOnMovieListAdded()
            is Event.OnFilterApplied -> handleOnFilterApplied(event.genre)
        }
    }

    private fun handleOnGetGenreList() {
        viewModelScope.launch {
            try {
                val result = getGenreListUseCase()

                _uiState.update {
                    it.copy(genreList = result)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e)
                }
            }
        }
    }

    private fun handleOnDiscoverMovieList() {
        val hasMoreData = _uiState.value.currentPage < _uiState.value.totalPages
        if (_uiState.value.isLoading || hasMoreData.not()) {
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                currentPage = it.currentPage + 1
            )
        }

        viewModelScope.launch {
            try {
                val genreIds = _uiState.value.selectedGenre?.let { listOf(it.id) }
                val result = discoverMovieUseCase(
                    page = _uiState.value.currentPage,
                    genreIds = genreIds
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        movieList = result.results,
                        currentPage = result.page,
                        totalPages = result.totalPages,
                        hasAddMovieList = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e,
                    )
                }
            }
        }
    }

    private fun handleOnMovieListAdded() {
        _uiState.update {
            it.copy(hasAddMovieList = true)
        }
    }

    private fun handleOnFilterApplied(genre: GenreDto) {
        _uiState.update {
            it.resetMovieList().copy(
                selectedGenre = genre
            )
        }

        handleOnDiscoverMovieList()
    }

}
