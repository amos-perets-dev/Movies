package com.example.movies.repo.images

import android.graphics.Bitmap
import com.example.movies.model.movie.MovieDetailsListResponse
import io.reactivex.Observable

interface IImagesRepository {

    fun getImagesAsync(): Observable<Map<String, Bitmap>>?

    fun initImages(): Observable<Boolean>

    fun loadPicture(movieDetailsListResponse: MovieDetailsListResponse)
}