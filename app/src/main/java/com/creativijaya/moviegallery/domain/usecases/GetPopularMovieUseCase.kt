package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.BasePaginationDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.domain.toDto
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.orZero
import com.creativijaya.moviegallery.utils.successOrError

class GetPopularMovieUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(page: Int = 1): BasePaginationDto<MovieDto> {
        return successOrError {
            service.getPopularMovies(page = page)
        }.mapTo {
            BasePaginationDto(
                page = it.page.orZero(),
                totalPages = it.totalPages.orZero(),
                totalResults = it.totalResults.orZero(),
                results = it.results?.map(MovieResponse::toDto).orEmpty()
            )
        }
    }
}
