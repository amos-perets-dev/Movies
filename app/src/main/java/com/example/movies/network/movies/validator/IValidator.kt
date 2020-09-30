package com.example.movies.network.movies.validator

interface IValidator {

    /**
     * Check if is valid size page of the movies
     */
    fun isValidSizePage(totalPage: Int) : Boolean

    /**
     * Check if need to update the local images
     */
    fun isNeedUpdateImages() : Boolean
}