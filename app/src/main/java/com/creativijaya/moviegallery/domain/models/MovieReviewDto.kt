package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieReviewDto(
    val id: String = "",
    val content: String = "",
    val author: String = "",
    val authorAvatarPath: String = "",
    val url: String = "",
    val createdAt: String = ""
) : Parcelable
