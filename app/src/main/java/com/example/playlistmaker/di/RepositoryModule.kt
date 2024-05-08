package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.repository.FavoriteTracksRepositoryImp
import com.example.playlistmaker.media.data.converters.TracksDbConvertor
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
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


}