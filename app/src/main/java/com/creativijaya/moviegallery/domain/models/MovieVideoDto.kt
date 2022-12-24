package com.creativijaya.moviegallery.domain.models

import android.os.Parcelable
import com.creativijaya.moviegallery.domain.models.enums.MovieVideoSiteType
import com.creativijaya.moviegallery.domain.models.enums.MovieVideoType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieVideoDto(
    val id: String = "",
    val key: String = "",
    val name: String = "",
    val type: MovieVideoType = MovieVideoType.UNKNOWN,
    val site: MovieVideoSiteType = MovieVideoSiteType.UNKNOWN
) : Parcelable
