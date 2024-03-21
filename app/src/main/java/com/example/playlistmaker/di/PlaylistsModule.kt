package com.example.playlistmaker.di

import com.example.playlistmaker.media.ui.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsModule = module {
    viewModel { PlaylistFragmentViewModel() }
}