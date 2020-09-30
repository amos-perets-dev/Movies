package com.example.movies.screen.movies_fav_list

import com.example.movies.screen.base_movies_activity.BaseMoviesActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MoviesFavListActivity : BaseMoviesActivity() {

    private val moviesFavListViewModel: MoviesFavListViewModel by viewModel()

    override fun getViewModel() = moviesFavListViewModel

}