package com.example.movies.repo.fav_movies

import com.example.movies.model.movie.MovieItem
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MoviesFavoritesRepository() : IMoviesFavoritesRepository {

    private val moviesListData = BehaviorSubject.create<ArrayList<MovieItem>>()
    private val moviesList = arrayListOf<MovieItem>()

    override fun getMoviesData(): Observable<ArrayList<MovieItem>> = moviesListData.hide()

    override fun addFavMovies(movieItem: MovieItem?) {
        if (movieItem != null) {
            moviesList.add(movieItem)
            moviesListData.onNext(moviesList)
        }
    }

    override fun deleteMovie(movieItem: MovieItem?) {
        moviesList.remove(movieItem)
        moviesListData.onNext(moviesList)
    }

    override fun isMovieExist(movieItem: MovieItem?): Boolean {
        return moviesList.contains(movieItem)
    }
}