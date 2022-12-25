package com.creativijaya.moviegallery.presentation.home

import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto
import com.creativijaya.moviegallery.presentation.base.BaseUiState

data class HomeUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val movieList: List<MovieDto> = emptyList(),
    val genreList: List<GenreDto> = emptyList(),
    val selectedGenre: GenreDto? = null,
    val hasAddMovieList: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: Exception? = null
) : BaseUiState {

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
