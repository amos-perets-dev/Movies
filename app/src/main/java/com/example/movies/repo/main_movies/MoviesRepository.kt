package com.example.movies.repo.main_movies

import android.graphics.Bitmap
import android.util.Log
import com.example.movies.model.movie.MovieDetailsListResponse
import com.example.movies.model.movie.MovieDetailsResponse
import com.example.movies.model.movie.MovieItem
import com.example.movies.network.movies.manager.IMoviesNetworkManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MoviesRepository(
    private val moviesNetworkManager: IMoviesNetworkManager,
    private val imagesData: Observable<Map<String, Bitmap>>?
) : IMoviesRepository {

    private val moviesList = arrayListOf<MovieItem>()
    private val mapMovies = HashMap<Long, MovieItem>()
    private val moviesListData = BehaviorSubject.create<ArrayList<MovieItem>>()

    private fun addMovies(movies: Array<in MovieDetailsListResponse>) {

        movies.forEach { movieDetailsResponse ->
            (movieDetailsResponse as MovieDetailsListResponse).moviesList?.let { moviesList ->

                val moviesDataList =
                    moviesList
                        .map(this::createMovieItemObject)
                        .toList()

                val elements = ArrayList(moviesDataList)
                this.moviesList.addAll(elements)
            }
        }
        moviesListData.onNext(this.moviesList)

        moviesList.forEach {
            mapMovies[it.movieId ?: 0] = it
        }
    }

    override fun getMoviesDataMap() = mapMovies

    override fun getMoviesData(): Observable<ArrayList<MovieItem>> =
        moviesListData.hide()

    override fun initData(): Observable<Boolean>? {
        return moviesNetworkManager
            .getDataMovies()
            ?.toObservable()
            ?.doOnNext(this::addMovies)
            ?.map { true }

    }

    private fun createMovieItemObject(movieDetailsResponse: MovieDetailsResponse): MovieItem {
        return MovieItem(
            movieDetailsResponse.movieId,
            movieDetailsResponse.nameTitle.toString(),
            imagesData,
            movieDetailsResponse.overview.toString(),
            movieDetailsResponse.rating.toString(),
            movieDetailsResponse.releaseDate.toString(),
            movieDetailsResponse.imagePath
        )
    }

}