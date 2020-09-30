package com.example.movies.repo.images

import android.graphics.Bitmap
import io.reactivex.Completable
import io.reactivex.Observable

interface IImagesRepository {

    fun addImages(): Observable<Boolean>?

    fun getImagesAsync(): Observable<Map<Long, Bitmap>>?

    fun initImages(): Observable<Boolean>
}