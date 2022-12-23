package com.creativijaya.moviegallery.utils

import com.creativijaya.moviegallery.BuildConfig

object MovieUtil {

    fun getImageUrl(path: String): String {
        return BuildConfig.IMAGE_BASE_URL + path
    }

}
