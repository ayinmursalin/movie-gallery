package com.creativijaya.moviegallery.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.domain.usecases.GetPopularMovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getGenreListUseCase: GetGenreListUseCase,
    private val getPopularMovieUseCase: GetPopularMovieUseCase
) : ViewModel() {

    sealed class Event {
        object OnGetPopularMovieList : Event()
    }

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            Event.OnGetPopularMovieList -> getMovieList()
        }
    }

    private fun getMovieList() {
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
                val result = getPopularMovieUseCase(page = _uiState.value.currentPage)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        movieList = result.results,
                        currentPage = result.page,
                        totalPages = result.totalPages
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

}
