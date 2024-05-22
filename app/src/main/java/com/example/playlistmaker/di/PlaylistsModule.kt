package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsModule = module {
    viewModel { PlaylistMakerViewModel(get()) }
}