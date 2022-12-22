package com.creativijaya.moviegallery.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GetGenreResponse(
    @SerializedName("genres")
    val genres: List<GenreResponse>? = null
)
