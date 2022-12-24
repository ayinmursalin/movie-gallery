package com.creativijaya.moviegallery.presentation.home

import com.creativijaya.moviegallery.domain.models.GenreDto

interface HomeFilterDialogListener {
    fun onFilterApplied(genre: GenreDto)
}
