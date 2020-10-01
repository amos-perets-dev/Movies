package com.example.movies.network.movies.manager

import android.graphics.Bitmap
import com.example.movies.model.movie.MovieDetailsListResponse
import io.reactivex.Flowable
import io.reactivex.Observable

interface IMoviesNetworkManager {

    /**
     * Get all the data movies
     */
    fun getDataMovies(): Flowable<Array<in MovieDetailsListResponse>>?

}