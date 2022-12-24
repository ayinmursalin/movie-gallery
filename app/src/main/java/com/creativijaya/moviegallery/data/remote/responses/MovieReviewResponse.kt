package com.creativijaya.moviegallery.data.remote.responses

import com.google.gson.annotations.SerializedName

data class MovieReviewResponse(
	@SerializedName("id")
	val id: String? = null,
	@SerializedName("author_details")
	val authorDetails: ReviewAuthorResponse? = null,
	@SerializedName("author")
	val author: String? = null,
	@SerializedName("content")
	val content: String? = null,
	@SerializedName("url")
	val url: String? = null,
	@SerializedName("created_at")
	val createdAt: String? = null,
	@SerializedName("updated_at")
	val updatedAt: String? = null
)
