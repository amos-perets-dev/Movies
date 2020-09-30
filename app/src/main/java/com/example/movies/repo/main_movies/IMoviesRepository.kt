package com.example.movies.repo.main_movies

import android.graphics.Bitmap
import com.example.movies.model.movie.MovieItem
import io.reactivex.Completable
import io.reactivex.Observable

interface IMoviesRepository {

    fun getMoviesDataMap() : HashMap<Long, MovieItem>

    fun getMoviesData(): Observable<ArrayList<MovieItem>>

    fun initData(): Observable<Boolean>?

}