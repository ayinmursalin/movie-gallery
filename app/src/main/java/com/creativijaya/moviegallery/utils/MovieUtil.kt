package com.creativijaya.moviegallery.utils

import com.creativijaya.moviegallery.BuildConfig
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieOrderType
import com.creativijaya.moviegallery.domain.models.enums.DiscoverMovieSortedType

object MovieUtil {

    fun getSortedBy(
        sortedType: DiscoverMovieSortedType = DiscoverMovieSortedType.POPULARITY,
        orderType: DiscoverMovieOrderType = DiscoverMovieOrderType.DESC
    ): String {
        return "${sortedType.key}.${orderType.key}"
    }

    fun getImageUrl(path: String): String {
        return BuildConfig.IMAGE_BASE_URL + path
    }

}
