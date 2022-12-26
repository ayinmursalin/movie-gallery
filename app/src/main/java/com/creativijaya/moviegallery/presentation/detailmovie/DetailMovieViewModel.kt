package com.creativijaya.moviegallery.presentation.detailmovie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativijaya.moviegallery.domain.usecases.GetMovieDetailUseCase
import com.creativijaya.moviegallery.domain.usecases.GetMovieReviewsUseCase
import com.creativijaya.moviegallery.utils.orZero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailMovieViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase
) : ViewModel() {

    sealed class Event {
        object OnGetMovieDetail : Event()
        object OnGetMovieReviews : Event()
    }

    private val _uiState = MutableStateFlow(DetailMovieUiState())

    val uiState: StateFlow<DetailMovieUiState>
        get() = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(movieId = savedStateHandle.get<Long>("movie_id").orZero())
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.OnGetMovieDetail -> handleOnGetMovieDetail()
            Event.OnGetMovieReviews -> handleOnGetMovieReviews()
        }
    }

    private fun handleOnGetMovieDetail() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
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

    private fun handleOnGetMovieReviews() {
        val hasMoreData = _uiState.value.reviewCurrentPage < _uiState.value.reviewTotalPages
        if (_uiState.value.isLoadingReview || hasMoreData.not()) {
            return
        }

        _uiState.update {
            it.copy(
                isLoadingReview = true,
                reviewCurrentPage = it.reviewCurrentPage + 1
            )
        }

        viewModelScope.launch {
            try {
                val result = getMovieReviewsUseCase(
                    movieId = _uiState.value.movieId,
                    page = _uiState.value.reviewCurrentPage
                )

                _uiState.update {
                    it.copy(
                        isLoadingReview = false,
                        movieReviews = result.results,
                        reviewTotalPages = result.totalPages,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingReview = false,
                        errorReview = e
                    )
                }
            }
        }
    }

}
