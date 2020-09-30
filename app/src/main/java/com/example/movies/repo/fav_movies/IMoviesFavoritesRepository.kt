package com.example.movies.repo.fav_movies

import com.example.movies.model.movie.MovieItem
import io.reactivex.Observable

interface IMoviesFavoritesRepository {

    fun getMoviesData(): Observable<ArrayList<MovieItem>>

    fun addFavMovies(movieItem: MovieItem?)

    fun deleteMovie(movieItem: MovieItem?)

    fun isMovieExist(movieItem: MovieItem?): Boolean

}