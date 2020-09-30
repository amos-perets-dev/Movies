package com.example.movies.model.movie

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("id")
    val movieId: Long?,

    @SerializedName("poster_path")
    val imagePath: String?,

    @SerializedName("vote_average")
    val rating: Double?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("original_title")
    val nameTitle: String?
) {

}