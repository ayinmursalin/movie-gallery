package com.creativijaya.moviegallery.domain

import com.creativijaya.moviegallery.data.remote.responses.GenreResponse
import com.creativijaya.moviegallery.domain.models.GenreDto
import com.creativijaya.moviegallery.utils.orEmpty
import com.creativijaya.moviegallery.utils.orZero

fun GenreResponse.toDto() = GenreDto(
    id = this.id.orZero(),
    name = this.name.orEmpty()
)
