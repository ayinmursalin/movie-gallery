package com.creativijaya.moviegallery.domain.models.enums

enum class MovieVideoType(val key: String) {
    TRAILER("Trailer"),
    TEASER("Teaser"),
    BEHIND_THE_SCENES("Behind the Scenes"),
    FEATURETTE("Featurette"),
    UNKNOWN("-");

    companion object {
        @JvmStatic
        fun fromString(key: String?) = values().find {
            it.key == key
        } ?: UNKNOWN
    }
}
