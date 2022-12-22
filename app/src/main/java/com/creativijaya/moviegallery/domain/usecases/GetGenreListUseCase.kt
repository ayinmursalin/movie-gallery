package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.responses.GenreResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.toDto
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.successOrError

class GetGenreListUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(): List<GenreDto> {
        return successOrError {
            service.getGenreList()
        }.mapTo {
            it.genres?.map(GenreResponse::toDto).orEmpty()
        }
    }
}
