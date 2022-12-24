package com.creativijaya.moviegallery.domain.models.enums

enum class MovieVideoSiteType(val key: String) {
    YOUTUBE("YouTube"),
    UNKNOWN("-");

    companion object {
        @JvmStatic
        fun fromString(key: String?) = values().find {
            it.key == key
        } ?: UNKNOWN
    }
}
