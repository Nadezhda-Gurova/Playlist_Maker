package com.example.playlistmaker.search.di

import com.example.playlistmaker.media.ui.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksModule = module {
    viewModel { FavoriteTracksViewModel(get()) }
}
