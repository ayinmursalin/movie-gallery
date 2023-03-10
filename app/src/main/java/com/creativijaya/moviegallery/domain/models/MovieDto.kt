package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDto(
    val id: Long = 0,
    val overview: String = "",
    val title: String = "",
    val genreIds: List<Int> = emptyList(),
    val posterPath: String = "",
    val releaseDate: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
) : Parcelable
