package com.example.movies.network.base

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class BaseNetworkManager :IBaseNetworkManager{

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w300/"
    }

    private var retrofit: Retrofit? = null

    override fun buildRetrofit(): Retrofit? {
        if (retrofit == null) {

            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS).build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
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