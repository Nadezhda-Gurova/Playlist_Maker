package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient
import com.example.playlistmaker.search.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.search.data.repository.SearchTrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.search.domain.use_case.GetTracksListUseCase
import com.example.playlistmaker.search.domain.use_case.SearchTrackHistoryUseCase
import com.example.playlistmaker.search.domain.util.VIEWED_TRACK
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl

object Creator {
    fun provideSettingsInteractor(sharedPreferences: SharedPreferences): SettingsInteractor{
        return SettingsInteractorImpl(sharedPreferences)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context, ExternalNavigator(context))
    }

    fun provideSearchTrackHistoryUseCase(context: Context): SearchTrackHistoryUseCase {
        return SearchTrackHistoryUseCase(provideSearchTrackHistoryRepository(context))
    }

    fun provideGetTracksListUseCase(): GetTracksListUseCase {
        return GetTracksListUseCase(provideTracksRepository())
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