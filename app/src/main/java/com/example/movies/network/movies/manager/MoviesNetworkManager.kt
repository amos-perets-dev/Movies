package com.example.movies.network.movies.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.movies.R
import com.example.movies.model.movie.MovieDetailsListResponse
import com.example.movies.movie_app.MovieApplication
import com.example.movies.network.ConstSuffixUrl
import com.example.movies.network.MoviesApi
import com.example.movies.network.base.BaseNetworkManager
import com.example.movies.network.base.IBaseNetworkManager
import com.example.movies.network.movies.validator.IValidator
import com.example.movies.repo.images.IImagesRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class MoviesNetworkManager(
    baseNetworkManager: IBaseNetworkManager,
    private val validator: IValidator,
    private val imagesRepository: IImagesRepository
) : IMoviesNetworkManager {
    private var INTERVAL_MOVIES = 0
    private var MAX_SIZE_PAGE = 5

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

//        if (validator.isNeedUpdateImages()) {
//
//            movieDetailsListResponse.moviesList?.map { it.imagePath }?.toList()
//                ?.forEach { imagePath ->
//                    preloadImage(
//                        MovieApplication.context,
//                        "${BaseNetworkManager.BASE_IMAGE_URL}$imagePath",
//                        imagePath
//                    )
//                }
//        }

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
            ?.doOnNext(imagesRepository::loadPicture)
            ?.map(this::createObservableArray)
            ?.flatMap(this::createCombineLatest)

//    private fun preloadImage(
//        context: Context?,
//        url: String,
//        imagePath: String?
//    ) {
//        Log.d("TEST_GAME", "MoviesNetworkManager preloadImage url: $url")
//
//        Glide.with(context!!)
//            .asBitmap()
//            .load(url)
//            .into(object : CustomTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    Log.d("TEST_GAME", "MoviesNetworkManager preloadImage onResourceReady")
//                    imagesRepository.addImage(imagePath.toString(), resource)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//            })
//    }


    private fun getTotalPage(totalPage: Int): Int =
        if (totalPage > MAX_SIZE_PAGE) MAX_SIZE_PAGE else totalPage

    private fun createObservableArray(response: MovieDetailsListResponse): ArrayList<Flowable<MovieDetailsListResponse>> {
        val array = arrayListOf<Flowable<MovieDetailsListResponse>>(Flowable.just(response))

        val totalPage = getTotalPage(response.totalPage)
        if (validator.isValidSizePage(totalPage)) {
            for (index in 2..totalPage) {
                val element =
                    getLatestMovies(index)
                        ?.doOnNext(imagesRepository::loadPicture)

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

}