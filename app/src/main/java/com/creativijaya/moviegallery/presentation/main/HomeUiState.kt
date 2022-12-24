package com.creativijaya.moviegallery.presentation.main

import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto

data class HomeUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val movieList: List<MovieDto> = emptyList(),
    val genreList: List<GenreDto> = emptyList(),
    val selectedGenre: GenreDto? = null,
    val error: Exception? = null
) {
    val isSuccess: Boolean
        get() = isLoading.not() && error == null
    val isFailed: Boolean
        get() = error != null

    fun resetMovieList() = this.copy(
        isLoading = false,
        currentPage = 0,
        totalPages = 1,
        movieList = emptyList(),
    )

    fun resetState() = this.copy(
        isLoading = false,
        currentPage = 0,
        totalPages = 1,
        movieList = emptyList(),
        genreList = emptyList(),
        selectedGenre = null,
        error = null
    )
}
