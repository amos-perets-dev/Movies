package com.example.movies.screen.movies_list

import com.example.movies.screen.base_movies_activity.BaseMoviesActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MoviesListActivity : BaseMoviesActivity() {

    private val moviesListViewModel: MoviesListViewModel by viewModel()

    override fun getViewModel() = moviesListViewModel

}