package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.media.data.repository.MediaTracksRepositoryImpl
import com.example.playlistmaker.media.domain.interactor.MediaTracksInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaModule = module {
    single<MediaTracksInteractor> {
        MediaTracksInteractor(get())
    }
    single {
        androidContext().getSharedPreferences(
            MediaTracksRepositoryImpl.SAVED_TRACKS,
            Context.MODE_PRIVATE
        )
    }
}