package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import org.koin.dsl.module

val interactorModule = module {

    single<SearchHistoryInteractor> {
        SearchHistoryInteractor(get())
    }

    single<SearchInteractor> {
        SearchInteractor(get())
    }

}