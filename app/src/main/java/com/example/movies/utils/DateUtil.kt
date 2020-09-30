package com.example.movies.utils

class DateUtil {

    companion object {
        const val DAY = 24 * 60 * 60 * 1000
    }

    fun getCurrDatePlus24Hours(date: Long): Long {
        return date + DAY
    }

}