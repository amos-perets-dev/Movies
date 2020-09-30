package com.example.movies.network.base

import retrofit2.Retrofit

interface IBaseNetworkManager {

    fun buildRetrofit(): Retrofit?

}