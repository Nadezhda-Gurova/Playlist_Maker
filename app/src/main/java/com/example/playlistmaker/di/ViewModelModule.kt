package com.example.playlistmaker.di

import com.example.playlistmaker.player.ui.MediaPlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Locale

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        (zeroTime: String) ->
        MediaPlayerViewModel(zeroTime, get(), get(), get())
    }

    single {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

}