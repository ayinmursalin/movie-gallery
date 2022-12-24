package com.creativijaya.moviegallery.data.remote.responses

import com.google.gson.annotations.SerializedName

data class MovieVideoResponse(
	@SerializedName("id")
	val id: String? = null,
	@SerializedName("site")
	val site: String? = null,
	@SerializedName("size")
	val size: Int? = null,
	@SerializedName("iso_3166_1")
	val iso31661: String? = null,
	@SerializedName("name")
	val name: String? = null,
	@SerializedName("official")
	val official: Boolean? = null,
	@SerializedName("type")
	val type: String? = null,
	@SerializedName("published_at")
	val publishedAt: String? = null,
	@SerializedName("iso_639_1")
	val iso6391: String? = null,
	@SerializedName("key")
	val key: String? = null
)
