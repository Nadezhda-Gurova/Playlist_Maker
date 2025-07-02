package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.favorite.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteTracksModule = module {
    viewModel { FavoriteTracksViewModel(get()) }
}
