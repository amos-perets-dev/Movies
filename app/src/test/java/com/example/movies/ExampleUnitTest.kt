package com.example.movies

import com.example.movies.model.movie.MovieItem
import com.example.movies.movie_app.shared.SharedPerf
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val sharedPerf = SharedPerf()
    private val validator = com.example.movies.network.movies.validator.Validator(sharedPerf)

    @Test
    fun validator_test() {
        assertFalse(validator.isValidSizePage(0))
        assertFalse(validator.isValidSizePage(1))
        assertFalse(validator.isValidSizePage(-1))
        assertTrue(validator.isValidSizePage(100))
    }

    @Test
    fun checkEqualsItems(){
        val movieItem1 = MovieItem(5)
        val movieItem2 = MovieItem(5)
        assertTrue(movieItem1 == movieItem2)

        val movieIte3 = MovieItem(7)
        val movieItem4 = MovieItem(54536)
        assertFalse(movieIte3 == movieItem4)
    }

}
