package com.creativijaya.moviegallery.data.remote.services

import com.creativijaya.moviegallery.data.remote.responses.BasePaginationResponse
import com.creativijaya.moviegallery.data.remote.responses.GetGenreResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("genre/movie/list")
    suspend fun getGenreList(): GetGenreResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): BasePaginationResponse<MovieResponse>

}
