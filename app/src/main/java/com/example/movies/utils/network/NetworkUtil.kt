package com.example.chekersgamepro.util.network

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

}