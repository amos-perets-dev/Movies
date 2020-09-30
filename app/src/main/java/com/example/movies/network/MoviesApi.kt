package com.example.movies.network

import com.example.movies.model.movie.MovieDetailsListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface MoviesApi {

    @GET
    fun getLatestMovies(@Url url: String): Observable<MovieDetailsListResponse>?

}