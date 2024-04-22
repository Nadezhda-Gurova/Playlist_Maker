package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.util.LoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(
    private val itunesNetworkClient: ITunesNetworkClient,
    private val mapper: TrackMapper
) : TracksRepository {
    override fun getTracks(track: String): Flow<LoadingState<List<Track>>> = flow {
        val itunesResponse = itunesNetworkClient.getTracks(track)
        when (itunesResponse.resultCode) {
            200 -> {
                emit(LoadingState.Success(mapper.map(itunesResponse as ITunesResponse)))
            }
            else -> {
                emit(LoadingState.Error("Ошибка сервера"))
            }
        }
    }

}