package com.creativijaya.moviegallery.di

import com.creativijaya.moviegallery.presentation.detailmovie.DetailMovieViewModel
import com.creativijaya.moviegallery.presentation.main.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::HomeViewModel)

    viewModelOf(::DetailMovieViewModel)

}
