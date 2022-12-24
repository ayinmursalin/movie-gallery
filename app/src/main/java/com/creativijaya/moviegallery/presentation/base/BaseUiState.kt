package com.creativijaya.moviegallery.presentation.base

interface BaseUiState {
    val isLoading: Boolean
    val error: Exception?

    val isSuccess: Boolean
        get() = isLoading.not() && error == null

    val isFailed: Boolean
        get() = error != null
}
