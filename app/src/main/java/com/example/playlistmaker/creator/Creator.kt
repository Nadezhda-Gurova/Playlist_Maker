package com.example.playlistmaker.creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.data.repository.ITunesNetworkClient
import com.example.playlistmaker.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.data.repository.SearchTrackHistoryRepositoryImpl
import com.example.playlistmaker.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.GetTracksListUseCase
import com.example.playlistmaker.domain.use_case.SearchTrackHistoryUseCase
import com.example.playlistmaker.domain.util.VIEWED_TRACK

object Creator {

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