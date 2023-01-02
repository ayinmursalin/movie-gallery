package com.creativijaya.moviegallery.presentation.home

import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.domain.models.MovieDto

data class HomeUiState(
    val movieList: List<MovieDto?> = emptyList(),
    val genreList: List<GenreDto> = emptyList(),
    val selectedGenre: GenreDto? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
