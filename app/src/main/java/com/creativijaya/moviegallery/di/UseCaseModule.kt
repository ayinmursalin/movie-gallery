package com.creativijaya.moviegallery.di

import com.creativijaya.moviegallery.domain.usecases.GetGenreListUseCase
import com.creativijaya.moviegallery.domain.usecases.DiscoverMovieUseCase
import com.creativijaya.moviegallery.domain.usecases.GetMovieDetailUseCase
import com.creativijaya.moviegallery.domain.usecases.GetMovieReviewsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory {
        GetGenreListUseCase(get())
    }

    factory {
        DiscoverMovieUseCase(get())
    }

    factory {
        GetMovieDetailUseCase(get())
    }

    factory {
        GetMovieReviewsUseCase(get())
    }
}
