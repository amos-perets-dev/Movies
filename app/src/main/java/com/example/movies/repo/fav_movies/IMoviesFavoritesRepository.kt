package com.example.movies.repo.fav_movies

import com.example.movies.model.movie.MovieItem
import io.reactivex.Observable

interface IMoviesFavoritesRepository {

    /**
     * Get the fav moies list
     */
    fun getMoviesData(): Observable<ArrayList<MovieItem>>

    /**
     * Call when need to add moview th the fav list
     */
    fun addFavMovies(movieItem: MovieItem?)

    /**
     * Call when need to delete movie
     */
    fun deleteMovie(movieItem: MovieItem?)

    /**
     * Check if the movies exist in the users list
     */
    fun isMovieExist(movieItem: MovieItem?): Boolean

}