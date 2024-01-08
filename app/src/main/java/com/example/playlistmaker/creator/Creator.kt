package com.example.playlistmaker.creator

import android.app.Application
import android.app.UiModeManager
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.util.VIEWED_TRACK
import com.example.playlistmaker.settings.domain.DarkModeRepository
import com.example.playlistmaker.settings.data.DarkModeRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import java.text.SimpleDateFormat
import java.util.Locale

object Creator {
    private lateinit var application: Application

    fun setApplication(application: Application) {
        this.application = application
    }

    fun provideSimpleDateFormat(): SimpleDateFormat {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

    fun provideDarkModeRepository(uiModeManager: UiModeManager): DarkModeRepository {
        return DarkModeRepositoryImpl(
            application.getSharedPreferences(App.DARK_THEME_MODE, MODE_PRIVATE),
            uiModeManager
        )
    }

    fun provideSettingsInteractor(
        darkModeRepository: DarkModeRepository
    ): SettingsInteractor {
        return SettingsInteractorImpl(darkModeRepository)
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractor(provideSearchTrackHistoryRepository())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractor(provideTracksRepository())
    }

    private fun provideSearchTrackHistoryRepository(): SearchTrackHistoryRepository {
        return TrackHistoryRepositoryImpl(
            application.getSharedPreferences(
                VIEWED_TRACK,
                AppCompatActivity.MODE_PRIVATE
            )
        )
    }

    private fun provideTracksRepository(): TracksRepository {
        return ITunesRepositoryImpl(provideItunesNetworkClient(), provideMapper())
    }

    private fun provideMapper(): TrackMapper {
        return TrackMapper()
    }

    private fun provideItunesNetworkClient(): ITunesNetworkClient {
        return ITunesRetrofitNetworkClient()
    }
}