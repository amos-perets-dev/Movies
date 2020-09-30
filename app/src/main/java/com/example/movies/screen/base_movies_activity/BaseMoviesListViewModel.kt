package com.example.movies.screen.base_movies_activity

import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies.model.movie.MovieItem
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.functions.Functions

open class BaseMoviesListViewModel(
    private val moviesData: Observable<ArrayList<MovieItem>>,
    private val buttonTextNextPage: Int,
    private val intent: Intent
) :
    ViewModel() {

    private val moviesList = MutableLiveData<ArrayList<MovieItem>>()

    private val compositeDisposable = CompositeDisposable()

    fun getMoviesList(lifecycleOwner: LifecycleOwner): Observable<ArrayList<MovieItem>> =
        Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, moviesList))

    fun isDataEmpty(lifecycleOwner: LifecycleOwner): Observable<String>? =
        getMoviesList(lifecycleOwner)
            .map { it.isEmpty() }
            .filter(Functions.equalsWith(true))
            .map { "" }

    fun isShowMsg(lifecycleOwner: LifecycleOwner): Observable<Float> =
        getMoviesList(lifecycleOwner)
            .map { if (it.isEmpty()) 1F else 0F }

    init {
        compositeDisposable.add(
            moviesData
                .subscribe { moviesDataItems ->
                    moviesList.postValue(moviesDataItems)
                }
        )
    }

    fun init() {
        compositeDisposable.add(
            moviesData
                .subscribe { moviesDataItems ->
                    moviesList.postValue(moviesDataItems)
                }
        )
    }

    fun getButtonTextNextPage() = buttonTextNextPage

    fun getIntent() = intent

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}