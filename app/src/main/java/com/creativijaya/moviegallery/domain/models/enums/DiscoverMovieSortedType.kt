package com.creativijaya.moviegallery.domain.models.enums

enum class DiscoverMovieSortedType(val key: String) {
    POPULARITY("popularity"),
    RELEASE_DATE("release_date"),
    VOTE_AVERAGE("vote_average"),
    VOTE_COUNT("vote_count"),
    REVENUE("revenue");
}
