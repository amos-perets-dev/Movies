package com.example.movies.screen.movie_details

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies.R
import com.example.movies.model.movie.MovieItem
import com.example.movies.repo.fav_movies.IMoviesFavoritesRepository
import com.example.movies.repo.main_movies.IMoviesRepository
import com.example.movies.screen.base_movies_activity.MoviesListAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(
    private val moviesRepository: IMoviesRepository,
    private val moviesFavoritesRepository: IMoviesFavoritesRepository,
    private val onClick : Observable<MoviesListAdapter.DataClick>
) : ViewModel() {

    private var isOnClickSave = false

    private val compositeDisposable = CompositeDisposable()

    private val nameTitle = MutableLiveData<String>()
    private val overview = MutableLiveData<String>()
    private val rating = MutableLiveData<String>()
    private val releaseDate = MutableLiveData<String>()
    private val isMovieAllReadySaved = MutableLiveData<Boolean>()
    private val posterMovie = MutableLiveData<Bitmap>()
    private var currMovie: MovieItem? = null

    fun getMovieName(lifecycleOwner: LifecycleOwner) =
        createPublisherString(lifecycleOwner, nameTitle)

    fun getMovieOverview(lifecycleOwner: LifecycleOwner) =
        createPublisherString(lifecycleOwner, overview)


    fun getMovieRating(lifecycleOwner: LifecycleOwner) =
        createPublisherString(lifecycleOwner, rating)

    fun getMovieReleaseDate(lifecycleOwner: LifecycleOwner) =
        createPublisherString(lifecycleOwner, releaseDate)


    fun getMovieImage(lifecycleOwner: LifecycleOwner): Observable<Bitmap> =
        createPublisher(lifecycleOwner, posterMovie)
            .cast(Bitmap::class.java)

    fun isMovieAllReadySaved(lifecycleOwner: LifecycleOwner): Observable<Int> =
        createPublisher(lifecycleOwner, isMovieAllReadySaved)
            .map{
               return@map if (isOnClickSave) {
                    R.drawable.ic_saved
                } else {
                    R.drawable.ic_to_save
                }
            }


    private fun createPublisherString(
        lifecycleOwner: LifecycleOwner,
        mutableLiveData: MutableLiveData<*>
    ): Observable<String> {
        return createPublisher(lifecycleOwner, mutableLiveData)
            .cast(String::class.java)
    }

    private fun createPublisher(
        lifecycleOwner: LifecycleOwner,
        mutableLiveData: MutableLiveData<*>
    ): Observable<Any> {
        return Observable.fromPublisher(
            LiveDataReactiveStreams.toPublisher(
                lifecycleOwner,
                mutableLiveData
            )
        )
    }

    init {
        compositeDisposable.add(
            onClick
                .subscribe {data ->
                    val moviesFromMap = moviesRepository
                        .getMoviesDataMap()[data.movieId]
                    currMovie = moviesFromMap
                    val movieExist = moviesFavoritesRepository.isMovieExist(moviesFromMap)
                    isOnClickSave = movieExist
                    notifyDetails(moviesFromMap, movieExist)
                }
        )

    }

    private fun notifyDetails(
        movieData: MovieItem?,
        movieExist: Boolean
    ) {
        val nameTitle = movieData?.nameTitle.toString()
        val overview = movieData?.overview.toString()
        val rating = movieData?.rating.toString()
        val releaseDate = movieData?.releaseDate.toString()

        this.nameTitle.postValue(nameTitle)
        this.overview.postValue(overview)
        this.rating.postValue(rating)
        this.releaseDate.postValue(releaseDate)

        this.isMovieAllReadySaved.postValue(movieExist)


        movieData
            ?.loadPicture()
            ?.subscribe { image ->
                this.posterMovie.postValue(image)
            }?.let {
                compositeDisposable.add(
                    it
                )
            }

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun onClickSave(): Int {
        isOnClickSave = !isOnClickSave

        if (isOnClickSave) {
            moviesFavoritesRepository.addFavMovies(currMovie)

        } else {
            moviesFavoritesRepository.deleteMovie(currMovie)

        }

        return if (isOnClickSave) {
            R.drawable.ic_saved
        } else {
            R.drawable.ic_to_save
        }
    }

}