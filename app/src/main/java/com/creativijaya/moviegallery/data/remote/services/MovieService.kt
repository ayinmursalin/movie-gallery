package com.creativijaya.moviegallery.data.remote.services

import com.creativijaya.moviegallery.data.remote.responses.GetGenreResponse
import retrofit2.http.GET

interface MovieService {

    @GET("genre/movie/list")
    suspend fun getGenreList(): GetGenreResponse

}
