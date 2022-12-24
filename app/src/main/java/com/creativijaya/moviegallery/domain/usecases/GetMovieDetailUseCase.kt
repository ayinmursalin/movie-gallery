package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.domain.toMovieDetailDto
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.successOrError

class GetMovieDetailUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(movieId: Long): MovieDetailDto {
        return successOrError {
            service.getMovieDetail(movieId)
        }.mapTo {
            it.toMovieDetailDto()
        }
    }
}
