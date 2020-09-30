package com.example.movies.movie_app.shared

import android.content.Context
import android.content.SharedPreferences
import com.example.movies.movie_app.MovieApplication

class SharedPerf : ISharedPerf{

    companion object{
        const val LAST_UPDATE_FILE = "last_update_file"
        const val LAST_UPDATE = "last_update"

    }

    private var mSharedPref: SharedPreferences? = null

    init {
        mSharedPref = MovieApplication.context?.getSharedPreferences(
            LAST_UPDATE_FILE,
            Context.MODE_PRIVATE
        )
    }

    override fun addLastUpdate(date : Long){
        mSharedPref?.edit()?.putLong(LAST_UPDATE, date)?.apply()
    }

    override fun getLastUpdate(): Long? {
        return mSharedPref?.getLong(LAST_UPDATE, 0)
    }



}