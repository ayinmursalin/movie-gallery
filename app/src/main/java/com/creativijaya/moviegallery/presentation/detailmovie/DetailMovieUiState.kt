package com.creativijaya.moviegallery.presentation.detailmovie

import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.presentation.base.BaseUiState

data class DetailMovieUiState(
    val movieId: Long = 0L,
    val movieDetail: MovieDetailDto = MovieDetailDto(),
    override val isLoading: Boolean = false,
    override val error: Exception? = null
) : BaseUiState {
    override val isSuccess: Boolean
        get() = super.isSuccess && movieDetail.id != 0L

    fun resetState() = this.copy(
        isLoading = false,
        movieDetail = MovieDetailDto(),
        error = null
    )
}
