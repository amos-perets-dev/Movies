package com.example.movies.model.movie

import android.graphics.Bitmap
import com.google.common.base.Optional
import io.reactivex.Observable

class MovieItem(
    val movieId: Long? = 0L,
    val nameTitle: String? = "",
    private val imagesData: Observable<Map<String, Bitmap>>? = null,
    val overview: String? = "",
    val rating: String? = "",
    val releaseDate: String? = "",
    private val imagePath: String? = ""

) {

    fun loadPicture(): Observable<Bitmap>? {
        return imagesData
            ?.map { Optional.fromNullable (it[imagePath]) }
            ?.filter { it.isPresent }
            ?.map { it.get() }
    }

    override fun equals(other: Any?): Boolean {
        if ((other is MovieItem).not()) return false

        return movieId == (other as MovieItem).movieId
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}