package com.creativijaya.moviegallery.domain

import com.creativijaya.moviegallery.data.remote.responses.GenreResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.utils.orEmpty
import com.creativijaya.moviegallery.utils.orFalse
import com.creativijaya.moviegallery.utils.orZero

fun GenreResponse.toDto() = GenreDto(
    id = this.id.orZero(),
    name = this.name.orEmpty()
)

fun MovieResponse.toDto() = MovieDto(
    id = this.id.orZero(),
    overview = this.overview.orEmpty(),
    originalLanguage = this.overview.orEmpty(),
    originalTitle = this.overview.orEmpty(),
    video = this.video.orFalse(),
    title = this.title.orEmpty(),
    genreIds = this.genreIds.orEmpty(),
    posterPath = this.posterPath.orEmpty(),
    backdropPath = this.backdropPath.orEmpty(),
    releaseDate = this.releaseDate.orEmpty(),
    popularity = this.popularity.orZero(),
    voteAverage = this.voteAverage.orZero(),
    adult = this.adult.orFalse(),
    voteCount = this.voteCount.orZero()
)
