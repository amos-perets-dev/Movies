package com.example.movies.screen.base_movies_activity

import android.view.ViewGroup
import com.example.movies.R
import com.example.movies.model.movie.MovieItem
import com.example.movies.movie_app.recyler_view.MovieRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieItemViewHolder(
    parent: ViewGroup,
    private val onClickItem: BehaviorSubject<MoviesListAdapter.DataClick>
) : MovieRecyclerView.Companion.ViewHolder<MovieItem>(parent, R.layout.movie_item) {

    init {
        itemView.poster_image_view.setOnClickListener { notifyClick() }
    }

    override fun bindData(model: MovieItem) {
        super.bindData(model)

        model.loadPicture()
            ?.subscribe {
                itemView.poster_image_view.setImageBitmap(it)
            }?.let {
                compositeDisposable.add(
                    it
                )
            }

    }

    override fun unBindData(model: MovieItem) {
        compositeDisposable.clear()
        super.unBindData(model)
    }

    private fun notifyClick() {
        onClickItem.onNext(
            MoviesListAdapter.DataClick(
                adapterPosition, getDataModel()?.movieId ?: 0
            )
        )
    }

    override fun destroy() {
        compositeDisposable.clear()
    }

}