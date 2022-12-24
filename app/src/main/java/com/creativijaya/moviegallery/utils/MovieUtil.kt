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
        if (path.isEmpty()) return ""

        return BuildConfig.IMAGE_BASE_URL + path
    }

    fun getAvatarUrl(path: String): String {
        if (path.isEmpty()) return ""

        if (path.startsWith("/https")) return path.replaceFirst("/", "")

        return BuildConfig.IMAGE_BASE_URL + path
    }

}
