package com.example.playlistmaker.di

import android.app.UiModeManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.media.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.media.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.settings.data.DarkModeRepositoryImpl
import com.example.playlistmaker.settings.data.DarkModeRepositoryImpl.Companion.DARK_THEME_MODE
import com.example.playlistmaker.settings.domain.DarkModeRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DARK_THEME_SHARED_PREF = "dark-theme-shared-pref"

val interactorModule = module {

    single<SearchHistoryInteractor> {
        SearchHistoryInteractor(get())
    }

    single<SearchInteractor> {
        SearchInteractor(get())
    }

    single(named(DARK_THEME_SHARED_PREF)) {
        androidContext().applicationContext.getSharedPreferences(DARK_THEME_MODE, MODE_PRIVATE)
    }

    single {
        androidContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    }

    single<DarkModeRepository> {
        DarkModeRepositoryImpl(get(named(DARK_THEME_SHARED_PREF)), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    single<PlaylistMakerInteractor> {
        PlaylistInteractorImpl(get())
    }

}
