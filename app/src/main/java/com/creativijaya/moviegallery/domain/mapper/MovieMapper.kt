package com.creativijaya.moviegallery.domain.mapper

import com.creativijaya.moviegallery.data.remote.responses.GenreResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieReviewResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieVideoResponse
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDetailDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.domain.models.MovieReviewDto
import com.creativijaya.moviegallery.domain.models.MovieVideoDto
import com.creativijaya.moviegallery.domain.models.enums.MovieVideoSiteType
import com.creativijaya.moviegallery.domain.models.enums.MovieVideoType
import com.creativijaya.moviegallery.utils.orEmpty
import com.creativijaya.moviegallery.utils.orFalse
import com.creativijaya.moviegallery.utils.orZero

fun GenreResponse.toGenreDto() = GenreDto(
    id = this.id.orZero(),
    name = this.name.orEmpty()
)

fun MovieVideoResponse.toMovieVideoDto() = MovieVideoDto(
    id = this.id.orEmpty(),
    key= this.key.orEmpty(),
    name = this.name.orEmpty(),
    type = MovieVideoType.fromString(this.type),
    site = MovieVideoSiteType.fromString(this.site)
)

fun MovieResponse.toMovieDto() = MovieDto(
    id = this.id.orZero(),
    overview = this.overview.orEmpty(),
    title = this.title.orEmpty(),
    genreIds = this.genreIds.orEmpty(),
    posterPath = this.posterPath.orEmpty(),
    releaseDate = this.releaseDate.orEmpty(),
    voteAverage = this.voteAverage.orZero(),
    voteCount = this.voteCount.orZero()
)

fun MovieResponse.toMovieDetailDto() = MovieDetailDto(
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
    voteCount = this.voteCount.orZero(),
    genres = this.genres?.map(GenreResponse::toGenreDto).orEmpty(),
    videos = this.videos?.results?.map(MovieVideoResponse::toMovieVideoDto).orEmpty()
)

fun MovieReviewResponse.toMovieReviewDto() = MovieReviewDto(
    id = this.id.orEmpty(),
    content = this.content.orEmpty(),
    author = this.author.orEmpty(),
    authorAvatarPath = this.authorDetails?.avatarPath.orEmpty(),
    url = this.url.orEmpty(),
    createdAt = this.createdAt.orEmpty()
)
