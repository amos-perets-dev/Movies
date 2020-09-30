package com.example.movies.movie_app.shared

interface ISharedPerf {

    fun addLastUpdate(date : Long)
    fun getLastUpdate(): Long?
}