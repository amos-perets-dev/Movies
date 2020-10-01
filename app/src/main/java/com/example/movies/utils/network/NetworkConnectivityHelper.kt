package com.example.movies.utils.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.movies.movie_app.MovieApplication
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class NetworkConnectivityHelper(private val context: Context) : Disposable {

    private val isNetworkAvailable = BehaviorSubject.create<Boolean>()

    private var isDisposed = false

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val availableNetwork = NetworkUtil().isAvailableNetwork()
//            Log.d("TEST_GAME", "NetworkConnectivityHelper availableNetwork: $availableNetwork")

            isNetworkAvailable.onNext(availableNetwork)
        }
    }

    init {
        context.registerReceiver(
            broadcastReceiver,
            IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )
    }

    fun isNetworkAvailable(): Observable<Boolean> = isNetworkAvailable.hide().distinctUntilChanged()

    override fun dispose() {
        isDisposed = try {
            MovieApplication.context?.unregisterReceiver(broadcastReceiver)
            true
        } catch (ex: Exception) {
            false
        }
    }

    override fun isDisposed(): Boolean {
        return isDisposed
    }

}