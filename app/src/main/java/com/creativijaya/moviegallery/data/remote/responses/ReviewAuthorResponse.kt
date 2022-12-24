package com.creativijaya.moviegallery.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ReviewAuthorResponse(
    @SerializedName("avatar_path")
    val avatarPath: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("rating")
    val rating: Any? = null,
    @SerializedName("username")
    val username: String? = null
)
