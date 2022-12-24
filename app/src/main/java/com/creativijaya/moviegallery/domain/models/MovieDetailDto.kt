package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailDto(
    val id: Long = 0,
    val overview: String = "",
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val video: Boolean = false,
    val title: String = "",
    val genreIds: List<Int> = emptyList(),
    val posterPath: String = "",
    val backdropPath: String = "",
    val releaseDate: String = "",
    val popularity: Double = 0.0,
    val voteAverage: Double = 0.0,
    val adult: Boolean = false,
    val voteCount: Int = 0,
    val genres: List<GenreDto> = emptyList(),
    val videos: List<MovieVideoDto> = emptyList()
) : Parcelable
