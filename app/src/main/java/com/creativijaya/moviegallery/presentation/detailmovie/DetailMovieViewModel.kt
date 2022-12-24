package com.creativijaya.moviegallery.presentation.detailmovie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.usecases.GetMovieDetailUseCase
import com.creativijaya.moviegallery.utils.orZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailMovieViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    sealed class Event {

    }

    private val _uiState = MutableStateFlow(DetailMovieUiState())

    val uiState: StateFlow<DetailMovieUiState>
        get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(movieId = savedStateHandle.get<Long>("movie_id").orZero())
        }

        getVideoDetail()
    }

    private fun getVideoDetail() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val result = getMovieDetailUseCase(movieId = _uiState.value.movieId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        movieDetail = result,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e)
                }
            }
        }

    }

}
