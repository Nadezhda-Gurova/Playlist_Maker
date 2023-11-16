package com.example.playlistmaker.creator

import com.example.playlistmaker.data.network.ITunesRetrofitNetworkClient
import com.example.playlistmaker.data.repository.ITunesNetworkClient
import com.example.playlistmaker.data.repository.ITunesRepositoryImpl
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.GetTracksListUseCase

object Creator {

    fun provideGetTracksListUseCase(): GetTracksListUseCase {
        return GetTracksListUseCase(provideTracksRepository() )
    }

    private fun provideTracksRepository(): TracksRepository {
        return ITunesRepositoryImpl(provideItunesNetworkClient())
    }

    private fun provideItunesNetworkClient(): ITunesNetworkClient {
        return ITunesRetrofitNetworkClient()
    }
}