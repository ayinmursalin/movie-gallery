package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.responses.MovieReviewResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.mapper.toMovieReviewDto
import com.creativijaya.moviegallery.domain.models.BasePaginationDto
import com.creativijaya.moviegallery.domain.models.MovieReviewDto
import com.creativijaya.moviegallery.utils.Async
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.orZero
import com.creativijaya.moviegallery.utils.successOrError

class GetMovieReviewsUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(
        movieId: Long,
        page: Int = 1
    ): Async<BasePaginationDto<MovieReviewDto>> {
        return successOrError {
            service.getMovieReviews(
                movieId = movieId,
                page = page
            )
        }.mapTo {
            BasePaginationDto(
                page = it.invoke().page.orZero(),
                results = it.invoke().results?.map(MovieReviewResponse::toMovieReviewDto).orEmpty(),
                totalPages = it.invoke().totalPages.orZero(),
                totalResults = it.invoke().totalResults.orZero()
            )
        }
    }
}
