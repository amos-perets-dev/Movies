package com.example.movies.screen.movies_fav_list

import android.content.Intent
import com.example.movies.model.movie.MovieItem
import com.example.movies.screen.base_movies_activity.BaseMoviesListViewModel
import io.reactivex.Observable

class MoviesFavListViewModel(
    moviesData: Observable<ArrayList<MovieItem>>,
    activityMoviesListButtonMyListText: Int,
    intent: Intent
) :
    BaseMoviesListViewModel(moviesData, activityMoviesListButtonMyListText, intent){

}