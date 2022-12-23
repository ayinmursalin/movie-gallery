package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasePaginationDto<T : Parcelable>(
    val page: Int = 0,
    val totalPages: Int = 0,
    val totalResults: Int = 0,
    val results: List<T> = emptyList(),
) : Parcelable
