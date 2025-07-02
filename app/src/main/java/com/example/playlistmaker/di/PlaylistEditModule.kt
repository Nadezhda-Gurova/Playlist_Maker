package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.playlist_edit.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistEditModule = module {
    viewModel { EditPlaylistViewModel(get()) }
}