package com.example.movies.utils.network

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import com.example.movies.movie_app.MovieApplication


class NetworkUtil {

    public fun isAvailableNetwork(): Boolean {
        val connectivityManager =
            MovieApplication.context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

//    public fun isAvailableNetwork(): Boolean {
//        val connectivityManager =
//            MovieApplication.context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
//        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
//        if (activeNetwork != null) {
//            when (activeNetwork.type) {
//                ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE -> return true
//            }
//        }
//        return false
//    }

}