package com.creativijaya.moviegallery.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BasePaginationResponse<T>(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
    @SerializedName("results")
    val results: List<T>? = null,
)
