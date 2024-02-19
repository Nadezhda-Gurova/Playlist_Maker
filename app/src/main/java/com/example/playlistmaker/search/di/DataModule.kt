package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.adapter.TrackTypeAdapter
import com.example.playlistmaker.search.data.dto.TrackTime
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.IMDbApi
import com.example.playlistmaker.search.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create(
                    get())
            )
            .build()
    }

    single<IMDbApi> {
        get<Retrofit>().create(IMDbApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single<Gson> {
        GsonBuilder().registerTypeAdapter(TrackTime::class.java, TrackTypeAdapter()).create()
    }

    single<ITunesNetworkClient> { ITunesRetrofitNetworkClient(get()) }

    single<TrackMapper> { TrackMapper() }
}
