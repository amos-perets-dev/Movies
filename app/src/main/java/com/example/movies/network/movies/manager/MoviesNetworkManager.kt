package com.example.movies.network.movies.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.movies.R
import com.example.movies.model.ImagesDataList
import com.example.movies.model.movie.MovieDetailsListResponse
import com.example.movies.model.movie.MovieDetailsResponse
import com.example.movies.model.movie.MovieItem
import com.example.movies.movie_app.MovieApplication
import com.example.movies.network.ConstSuffixUrl
import com.example.movies.network.MoviesApi
import com.example.movies.network.base.BaseNetworkManager
import com.example.movies.network.base.IBaseNetworkManager
import com.example.movies.network.movies.validator.IValidator
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm

class MoviesNetworkManager(
    baseNetworkManager: IBaseNetworkManager,
    private val validator: IValidator
) : IMoviesNetworkManager {
    private var INTERVAL_MOVIES = 0
    private var MAX_SIZE_PAGE = 5

    private val imagesData: HashMap<Long, Bitmap> = HashMap()

    private val imagesReadyNotifier = BehaviorSubject.create<Map<Long, Bitmap>>()
    private val imagesCompleteNotifier = BehaviorSubject.create<Map<Long, Bitmap>>()

    private val baseApi = baseNetworkManager
        .buildRetrofit()
        ?.create(MoviesApi::class.java)

    private fun getLatestMovies(page: Int): Flowable<MovieDetailsListResponse>? {

        val url = generateGetLatestMoviesUrl(page)

        return baseApi
            ?.getLatestMovies(url)
            ?.subscribeOn(Schedulers.io())
            ?.toFlowable(BackpressureStrategy.MISSING)
            ?.onErrorReturn {
                MovieDetailsListResponse(
                    arrayListOf(),
                    0
                )
            }
            ?.doOnError {}
    }


    private fun generateGetLatestMoviesUrl(page: Int) =
        String.format(
            ConstSuffixUrl.GET_ALL_MOVIES,
            MovieApplication.context?.getString(R.string.tmdb_id), page.toString()
        )

    private fun loadPicture(movieDetailsListResponse: MovieDetailsListResponse) {

        if (validator.isNeedUpdateImages()){

            movieDetailsListResponse.moviesList?.forEach {
                preloadImage(
                    MovieApplication.context,
                    "${BaseNetworkManager.BASE_IMAGE_URL}${it.imagePath}",
                    it.movieId
                )
            }
        }


    }

    override fun getDataMovies() =
        getLatestMovies(1)
            ?.doOnNext {
                val totalPage = it.totalPage
                if (totalPage < MAX_SIZE_PAGE) {
                    MAX_SIZE_PAGE = totalPage
                }
            }
            ?.doOnNext { INTERVAL_MOVIES = it.moviesList?.size ?: 0 }
            ?.doOnNext(this::loadPicture)
            ?.map(this::createObservableArray)
            ?.flatMap(this::createCombineLatest)

    private fun preloadImage(
        context: Context?,
        url: String,
        movieId: Long?
    ) {
        Glide.with(context!!)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    imagesData[movieId ?: 0] = resource
                    imagesReadyNotifier.onNext(imagesData)

                    if (imagesData.size == INTERVAL_MOVIES * MAX_SIZE_PAGE) {
                        imagesCompleteNotifier.onNext(imagesData)
                    }
                }
            })
    }


    private fun getTotalPage(totalPage: Int): Int =
        if (totalPage > MAX_SIZE_PAGE) MAX_SIZE_PAGE else totalPage

    private fun createObservableArray(response: MovieDetailsListResponse): ArrayList<Flowable<MovieDetailsListResponse>> {
        val array = arrayListOf<Flowable<MovieDetailsListResponse>>(Flowable.just(response))

        val totalPage = getTotalPage(response.totalPage)
        if (validator.isValidSizePage(totalPage)) {
            for (index in 2..totalPage) {
                val element =
                    getLatestMovies(index)
                        ?.doOnNext(this::loadPicture)

                if (element != null) {
                    array.add(element)
                }
            }
        }

        return array
    }

    private fun createCombineLatest(array: ArrayList<Flowable<MovieDetailsListResponse>>): Flowable<Array<in MovieDetailsListResponse>>? {
        return Flowable.combineLatest(array) { results: Array<in MovieDetailsListResponse> ->
            results
        }
    }

    override fun getImagesDataAsync(): Observable<Map<Long, Bitmap>>? =
        imagesReadyNotifier.hide()
            .subscribeOn(Schedulers.io())

    override fun completeImages(): Observable<Map<Long, Bitmap>> =
        imagesCompleteNotifier.hide()
            .subscribeOn(Schedulers.io())

}