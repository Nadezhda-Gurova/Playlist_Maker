package com.example.playlistmaker.creator

import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.TimerInteractor
import com.example.playlistmaker.player.domain.TimerInteractorImpl
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.util.VIEWED_TRACK
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigatorRepository
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import java.text.SimpleDateFormat
import java.util.Locale

object Creator {

    fun provideSimpleDateFormat(): SimpleDateFormat {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

    fun provideTimerInteractor(
        player: MediaPlayer,
        handler: Handler,
        format: SimpleDateFormat,
        context: Context
    ): TimerInteractor {
        return TimerInteractorImpl(
            player = player,
            handler = handler,
            format = format,
            context = context
        )
    }

    fun provideMediaPlayerInteractor(player: MediaPlayer): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(player)
    }

    fun provideSettingsInteractor(
        uiModeManager: UiModeManager,
        sharedPrefs: SharedPreferences
    ): SettingsInteractor {
        return SettingsInteractorImpl(uiModeManager, sharedPrefs)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context, ExternalNavigatorRepository(context))
    }

    fun provideSearchTrackHistoryUseCase(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractor(provideSearchTrackHistoryRepository(context))
    }

    fun provideGetTracksListInteractor(): SearchInteractor {
        return SearchInteractor(provideTracksRepository())
    }

    private fun provideSearchTrackHistoryRepository(context: Context): SearchTrackHistoryRepository {
        return SearchTrackHistoryRepositoryImpl(
            context.getSharedPreferences(
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