package com.example.movies.model.movie

import com.google.gson.annotations.SerializedName

data class MovieDetailsListResponse(
    @SerializedName("results")
    val moviesList: List<MovieDetailsResponse>?,

    @SerializedName("total_pages")
    val totalPage: Int = 0
) {

}