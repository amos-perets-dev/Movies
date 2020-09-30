package com.example.movies.screen.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.movies.R
import com.example.movies.movie_app.MovieActivity
import com.example.movies.screen.movies_list.MoviesListActivity
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : MovieActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.init()

        val titleTextAnimate = title_text_view
            .isFinishAnimate()
            ?.doOnSubscribe { animateTitleText() }

        val dataReady = splashViewModel.isDataReady(this)



        compositeDisposable.addAll(
            Observable.combineLatest<Boolean, Boolean, Boolean>(
                titleTextAnimate,
                dataReady,
                BiFunction { t1, t2 -> t1 && t2})
                .subscribe {
                    startActivity(Intent(this, MoviesListActivity::class.java))
                    finish()
                },

            splashViewModel.isShowNetworkMsg(this)
                .subscribe {
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                },


            splashViewModel.isNetworkAvailable(this)
                .subscribe {
                    splashViewModel.initData()
                }


        )


    }

    private fun animateTitleText() {
        title_text_view.setCharacterDelay(400)
        title_text_view.animateText(getString(R.string.activity_splash_title_text))
    }
}