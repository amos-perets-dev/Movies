package com.example.movies.network.base

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class BaseNetworkManager :IBaseNetworkManager{

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500/"
    }

    private var retrofit: Retrofit? = null

    override fun buildRetrofit(): Retrofit? {
        if (retrofit == null) {

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        }
        return retrofit
    }

}