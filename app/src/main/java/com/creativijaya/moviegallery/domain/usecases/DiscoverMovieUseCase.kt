package com.creativijaya.moviegallery.domain.usecases

import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.services.MovieService
import com.creativijaya.moviegallery.domain.models.BasePaginationDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieOrderType
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieSortedType
import com.creativijaya.moviegallery.domain.mapper.toMovieDto
import com.creativijaya.moviegallery.utils.Async
import com.creativijaya.moviegallery.utils.MovieUtil
import com.creativijaya.moviegallery.utils.mapTo
import com.creativijaya.moviegallery.utils.orZero
import com.creativijaya.moviegallery.utils.successOrError

class DiscoverMovieUseCase(
    private val service: MovieService
) {
    suspend operator fun invoke(
        page: Int = 1,
        sortedType: DiscoverMovieSortedType = DiscoverMovieSortedType.POPULARITY,
        orderType: DiscoverMovieOrderType = DiscoverMovieOrderType.DESC,
        genreIds: List<Int>? = null
    ): Async<BasePaginationDto<MovieDto>> {
        return successOrError {
            val sortedBy = MovieUtil.getSortedBy(sortedType, orderType)
            val withGenreIds = genreIds?.joinToString(",")

            service.discoverMovies(
                page = page,
                sortedBy = sortedBy,
                withGenreIds = withGenreIds
            )
        }.mapTo {
            BasePaginationDto(
                page = it.invoke().page.orZero(),
                totalPages = it.invoke().totalPages.orZero(),
                totalResults = it.invoke().totalResults.orZero(),
                results = it.invoke().results?.map(MovieResponse::toMovieDto).orEmpty()
            )
        }
    }
}
