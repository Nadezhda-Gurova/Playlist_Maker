package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackTime
import com.example.playlistmaker.ui.recyclerview.TrackTypeAdapter
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(TrackTime::class.java, TrackTypeAdapter())
                        .create()
                )
            )
            .build()
    }

    val api: IMDbApi by lazy {
        client.create(IMDbApi::class.java)
    }
}