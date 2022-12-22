package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreDto(
    val id: Int = 0,
    val name: String = ""
) : Parcelable
