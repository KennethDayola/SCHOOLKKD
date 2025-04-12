package com.intprog.schoolkkd

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun createRetrofit(): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://astonishing-unruly-tomato.glitch.me")  // Your server URL
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}