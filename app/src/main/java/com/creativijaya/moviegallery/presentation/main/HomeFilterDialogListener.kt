package com.creativijaya.moviegallery.presentation.main

import com.creativijaya.moviegallery.domain.models.GenreDto

interface HomeFilterDialogListener {
    fun onFilterApplied(genre: GenreDto)
}
