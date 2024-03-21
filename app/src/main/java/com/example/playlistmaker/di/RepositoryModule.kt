package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get())
    }

    single<TracksRepository> {
        ITunesRepositoryImpl(get(), get())
    }


}