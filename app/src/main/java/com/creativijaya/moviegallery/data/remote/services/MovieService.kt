package com.creativijaya.moviegallery.data.remote.services

import com.creativijaya.moviegallery.data.remote.responses.BasePaginationResponse
import com.creativijaya.moviegallery.data.remote.responses.GetGenreResponse
import com.creativijaya.moviegallery.data.remote.responses.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("genre/movie/list")
    suspend fun getGenreList(): GetGenreResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortedBy: String? = null,
        @Query("with_genres") withGenreIds: String? = null
    ): BasePaginationResponse<MovieResponse>

}
