package com.example.movies.network.movies.validator

interface IValidator {

    fun isValidSizePage(totalPage: Int) : Boolean

    fun isNeedUpdateImages() : Boolean
}