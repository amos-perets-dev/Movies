package com.example.movies.screen.base_movies_activity

import android.view.ViewGroup
import com.example.movies.model.movie.MovieItem
import com.example.movies.movie_app.recyler_view.MovieRecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class MoviesListAdapter(private val moviesList: List<MovieItem> = arrayListOf()) :
    MovieRecyclerView.Companion.Adapter<MovieItem>(moviesList) {

    private val onClickItem = BehaviorSubject.create<DataClick>()

    override fun getItem(position: Int) = this.moviesList[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        addViewHolder(
            MovieItemViewHolder(
                parent,
                onClickItem
            )
        )

    override fun isNeedChangeItemSize() = true

    override fun isNeedChangeItemWidth() = true

    fun getItemClicked(): Observable<DataClick> = onClickItem.hide()
        .filter { dataClick -> dataClick.position != -1 }


    fun getCurrentName(): Observable<String> =
        getCurrentScrollPosition()
            .filter { it != -1 }
            .map { position -> getDataList()[position] }
            .map(MovieItem::nameTitle)
            .map(String::toString)

    fun init() {
        onClickItem.onNext(DataClick(-1, -1L))
    }

    class DataClick(val position: Int, val movieId: Long)

}