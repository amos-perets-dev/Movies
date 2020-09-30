package com.example.movies.movie_app.shared

interface ISharedPerf {

    /**
     * add the last update of the images
     */
    fun addLastUpdate(date : Long)

    /**
     * Get the last update of the images
     */
    fun getLastUpdate(): Long?
}