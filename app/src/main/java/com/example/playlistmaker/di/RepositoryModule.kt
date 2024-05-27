package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.converters.PlaylistsDbConvertor
import com.example.playlistmaker.media.data.converters.PlaylistsTracksDbConverter
import com.example.playlistmaker.media.data.repository.FavoriteTracksRepositoryImp
import com.example.playlistmaker.media.data.converters.TracksDbConvertor
import com.example.playlistmaker.media.data.repository.PlaylistMakerRepositoryImpl
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.google.gson.Gson
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(), get())
    }

    single<TracksRepository> {
        ITunesRepositoryImpl(get(), get(), get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImp(get(), get())
    }

    factory { TracksDbConvertor() }

    factory { PlaylistsDbConvertor(Gson()) }

    factory { PlaylistsTracksDbConverter() }

    single<PlaylistMakerRepository> { PlaylistMakerRepositoryImpl(get(), get(), get(), get()) }
}