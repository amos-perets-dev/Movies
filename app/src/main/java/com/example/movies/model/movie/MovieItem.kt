package com.example.movies.model.movie

import android.graphics.Bitmap
import io.reactivex.Observable

class MovieItem(
    val movieId: Long? = 0L,
    val nameTitle: String? = "",
    private val imagesData: Observable<Map<Long, Bitmap>>? = null,
    val overview: String? = "",
    val rating: String? = "",
    val releaseDate: String? = ""

) {

    fun loadPicture(): Observable<Bitmap?>? {
        return imagesData
            ?.map { it[movieId] }
    }

    override fun equals(other: Any?): Boolean {
        if ((other is MovieItem).not()) return false

        return movieId == (other as MovieItem).movieId
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}