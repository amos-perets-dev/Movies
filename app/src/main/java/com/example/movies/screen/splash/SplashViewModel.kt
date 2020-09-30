package com.example.movies.screen.splash

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chekersgamepro.util.network.NetworkUtil
import com.example.movies.R
import com.example.movies.movie_app.MovieApplication
import com.example.movies.network.movies.manager.IMoviesNetworkManager
import com.example.movies.network.movies.validator.IValidator
import com.example.movies.repo.images.IImagesRepository
import com.example.movies.repo.main_movies.IMoviesRepository
import com.example.movies.repo.images.ImagesRepository
import com.example.movies.utils.network.NetworkConnectivityHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.functions.Function4
import io.reactivex.internal.functions.Functions
import java.util.*

class SplashViewModel(
    private val moviesRepository: IMoviesRepository,
    private val imagesRepository: IImagesRepository,
    private val connectivityHelper: NetworkConnectivityHelper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val readyData = MutableLiveData<Boolean>()
    private val isNetwork = MutableLiveData<Boolean>()


    fun isDataReady(lifecycleOwner: LifecycleOwner): Observable<Boolean> {
        return Observable.fromPublisher(
            LiveDataReactiveStreams.toPublisher(
                lifecycleOwner,
                readyData
            )
        )
    }

    fun isShowNetworkMsg(lifecycleOwner: LifecycleOwner): Observable<String> {
        return Observable.fromPublisher(
            LiveDataReactiveStreams.toPublisher(
                lifecycleOwner,
                isNetwork
            )
        )
            .map { isNetworkAvailable ->
                MovieApplication.context?.getString(
                    if (isNetworkAvailable) R.string.network_msg_load else R.string.network_msg
                )
                    .toString()
            }
    }

    fun isNetworkAvailable(lifecycleOwner: LifecycleOwner): Observable<Boolean> {
        return Observable.fromPublisher(
            LiveDataReactiveStreams.toPublisher(
                lifecycleOwner,
                isNetwork
            )
        )
            .filter(Functions.equalsWith(true))
    }

    init {
        compositeDisposable.add(
            connectivityHelper
                .isNetworkAvailable()
                .startWith(NetworkUtil().isAvailableNetwork())
                .doOnDispose { connectivityHelper.dispose() }
                .doOnNext { isNetworkAvailable ->
                    isNetwork.postValue(isNetworkAvailable)
                }
                .filter(Functions.equalsWith(true))
                .subscribe {
                    isNetwork.postValue(true)
                }
        )
    }


    fun initData() {

        val initImages = imagesRepository
            .initImages()

        val initData = moviesRepository
            .initData()

        val addImages = imagesRepository.addImages()

        compositeDisposable.add(
            Observable.combineLatest<Boolean, Boolean, Boolean, Boolean>(
                initData,
                addImages,
                initImages,
                Function3 { t1, t2, t3 -> t1 && t2 && t3 })
                .subscribe {
                    readyData.postValue(true)
                }
        )
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun init() {

    }

}