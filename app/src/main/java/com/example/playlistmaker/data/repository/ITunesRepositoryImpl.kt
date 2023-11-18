package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.util.LoadingState

class ITunesRepositoryImpl(
    private val itunesNetworkClient: ITunesNetworkClient,
    private val mapper: TrackMapper
): TracksRepository {
    override fun getTracks(track:String): LoadingState<List<Track>> {
        val itunesResponse = itunesNetworkClient.getTracks(track)
        return if (itunesResponse is ITunesResponse) {
            LoadingState.Success(mapper.map(itunesResponse))
        } else {
            LoadingState.Error("Произошла сетевая ошибка")
        }
    }

}