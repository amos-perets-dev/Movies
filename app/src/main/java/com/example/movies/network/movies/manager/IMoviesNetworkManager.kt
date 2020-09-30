package com.example.movies.network.movies.manager

import android.graphics.Bitmap
import com.example.movies.model.movie.MovieDetailsListResponse
import io.reactivex.Flowable
import io.reactivex.Observable

interface IMoviesNetworkManager {

    /**
     * Get the images
     */
    fun getImagesDataAsync(): Observable<Map<Long, Bitmap>>?

    /**
     * Get all the data movies
     */
    fun getDataMovies(): Flowable<Array<in MovieDetailsListResponse>>?

    /**
     * Get the images when complete loaded
     */
    fun completeImages(): Observable<Map<Long, Bitmap>>
}