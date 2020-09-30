package com.example.movies.screen.base_movies_activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.movie_app.MovieActivity
import com.example.movies.movie_app.recyler_view.MovieLayoutManager
import com.example.movies.screen.movie_details.MovieDetailsFragment
import kotlinx.android.synthetic.main.activity_base_movies_list.*
import org.koin.android.ext.android.inject

abstract class BaseMoviesActivity : MovieActivity() {

    private val baseMoviesListViewModel: BaseMoviesListViewModel by lazy {
        getViewModel()
    }

    private val moviesListAdapter: MoviesListAdapter by inject()
    private val movieLayoutManager: MovieLayoutManager by inject()

    var isPickerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_base_movies_list)

        moviesListAdapter.init()

        val moviesListRecyclerView = movies_list_recycler_view

        compositeDisposable.addAll(

            baseMoviesListViewModel
                .isDataEmpty(this)
                ?.subscribe(movie_name_header::setText),

            baseMoviesListViewModel
                .isShowMsg(this)
                .subscribe(this::animateMsg),


            baseMoviesListViewModel
                .getMoviesList(this)
                .subscribe { data ->
                    moviesListRecyclerView.layoutManager = movieLayoutManager
                    moviesListRecyclerView.adapter = moviesListAdapter

                    moviesListAdapter.updateList(data)
                    moviesListAdapter.scrollToPositionWithOffset(LinearLayoutManager.HORIZONTAL)
                },

            moviesListAdapter.getCurrentName()
                .subscribe(movie_name_header::setText),

            moviesListAdapter
                .getItemClicked()
                .map(MoviesListAdapter.DataClick::position)
                .subscribe (this::actionByClick)

        )


        button_next_page_text_view.text = getString(baseMoviesListViewModel.getButtonTextNextPage())

        button_next_page_text_view.setOnClickListener {
            startActivity(baseMoviesListViewModel.getIntent())
            finish()
        }

    }

    private fun animateMsg(alpha: Float?) {
        msg_text_view.animate().alpha(alpha ?: 0F).setDuration(200).start()
    }

    private fun actionByClick(position: Int) {
        if (position == moviesListAdapter.getCurrentPosition(movieLayoutManager) && isPickerOpen.not()) {
            MovieDetailsFragment().show(supportFragmentManager, "DETAILS")
            isPickerOpen = true
        } else{
            movieLayoutManager.scrollToCenter(position)
        }
    }

    override fun onDestroy() {
        moviesListAdapter.dispose()
        super.onDestroy()
    }

    abstract fun getViewModel(): BaseMoviesListViewModel

}

