package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.responses.MovieReviewResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.mapper.toMovieReviewDto
import com.creativijaya.moviegallery.domain.models.MovieReviewDto
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.successOrError

class GetMovieReviewsUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(
        movieId: Long,
        page: Int = 1
    ): List<MovieReviewDto> {
        return successOrError {
            service.getMovieReviews(
                movieId = movieId,
                page = page
            )
        }.mapTo {
            it.results?.map(MovieReviewResponse::toMovieReviewDto).orEmpty()
        }
    }
}
