package com.creativijaya.moviegallery.di

import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.domain.usecases.GetPopularMovieUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetGenreListUseCase(get())
    }

    factory {
        GetPopularMovieUseCase(get())
    }
}
