package com.creativijaya.moviegallery.presentation.detailmovie

import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.domain.models.MovieReviewDto
import com.creativijaya.moviegallery.presentation.base.BaseUiState

data class DetailMovieUiState(
    val movieId: Long = 0L,
    val movieDetail: MovieDetailDto = MovieDetailDto(),
    val reviewCurrentPage: Int = 0,
    val reviewTotalPages: Int = 1,
    val movieReviews: List<MovieReviewDto> = emptyList(),
    val isLoadingReview: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: Exception? = null
) : BaseUiState {
    override val isSuccess: Boolean
        get() = super.isSuccess && movieDetail.id != 0L

    val isSuccessGetReviews: Boolean
        get() = isLoadingReview.not() && movieReviews.isNotEmpty()

    fun resetState() = this.copy(
        isLoading = false,
        movieDetail = MovieDetailDto(),
        error = null
    )
}
