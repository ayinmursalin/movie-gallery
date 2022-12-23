package com.creativijaya.moviegallery.presentation.main

import com.creativijaya.moviegallery.domain.models.MovieDto

data class HomeUiState(
    val isLoading: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val movieList: List<MovieDto> = emptyList(),
    val error: Exception? = null
) {
    val isSuccess: Boolean
        get() = isLoading.not() && error == null
    val isFailed: Boolean
        get() = error != null
}
