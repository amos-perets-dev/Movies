package com.example.movies.network.movies.manager

import android.graphics.Bitmap
import com.example.movies.model.movie.MovieDetailsListResponse
import io.reactivex.Flowable
import io.reactivex.Observable

interface IMoviesNetworkManager {
    fun getImagesDataAsync(): Observable<Map<Long, Bitmap>>?
    fun getImagesData(): Map<Long, Bitmap>?
    fun getDataMovies(): Flowable<Array<in MovieDetailsListResponse>>?
    fun completeImages(): Observable<Map<Long, Bitmap>>
}