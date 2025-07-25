package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.PlaylistsAppDatabase
import com.example.playlistmaker.media.data.db.PlaylistsTracksDatabase
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

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "database.db")
            .build()
    }

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = PlaylistsAppDatabase::class.java,
            name = "playlist_base.db")
            .build()
    }

    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = PlaylistsTracksDatabase::class.java,
            name = "playlist_and_tracks_base.db")
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create(
                    get()
                )
            )
            .build()
    }

    single<IMDbApi> {
        get<Retrofit>().create(IMDbApi::class.java)
    }

    single<Gson> {
        GsonBuilder().registerTypeAdapter(TrackTime::class.java, TrackTypeAdapter()).create()
    }

    single<ITunesNetworkClient> { ITunesRetrofitNetworkClient(get()) }

    single<TrackMapper> { TrackMapper() }
}
