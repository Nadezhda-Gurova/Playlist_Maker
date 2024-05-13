package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TracksRepository
import com.example.playlistmaker.util.LoadingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesRepositoryImpl(
    private val itunesNetworkClient: ITunesNetworkClient,
    private val mapper: TrackMapper,
    private val appDatabase: AppDatabase,
) : TracksRepository {
    override fun getTracks(track: String): Flow<LoadingState<List<Track>>> = flow {
        val itunesResponse = itunesNetworkClient.getTracks(track)
        when (itunesResponse.resultCode) {
            200 -> {
                val updateFavorite = updateIsFavorite(
                    mapper.map(itunesResponse as ITunesResponse),
                    appDatabase.favoriteDao().getTracksId()
                )
                emit(LoadingState.Success(updateFavorite))
            }
            else -> {
                emit(LoadingState.Error("Ошибка сервера"))
            }
        }
    }
}


// Функция для обновления флага isFavorite в списке треков
fun updateIsFavorite(tracks: List<Track>, favoriteTrackIds: List<Int>): List<Track> {
    return tracks.map { track ->
        // Проверяем, присутствует ли идентификатор трека в списке избранных
        val isFavorite = favoriteTrackIds.contains(track.trackId)
        // Создаем копию трека с обновленным флагом isFavorite
        track.copy(favorite = isFavorite)
    }
}