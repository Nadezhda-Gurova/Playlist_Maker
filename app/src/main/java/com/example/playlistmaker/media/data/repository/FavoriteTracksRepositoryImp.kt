package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.converters.TracksDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteTracksRepositoryImp(
    private val appDatabase: AppDatabase,
    private val tracksDbConvertor: TracksDbConvertor,
) : FavoriteTracksRepository {

    var flow_ = MutableStateFlow<List<Track>>(emptyList())
    val flow: StateFlow<List<Track>> = flow_

    override suspend fun addTrack(track: Track) {
        val entity = convertFromEntityTrack(track)
        val curTime = System.currentTimeMillis()
        appDatabase.favoriteDao().insertTrack(entity.copy(timestamp = curTime, favorite = true))
        flow_.emit(listOf(track.copy(timestamp = curTime, favorite = true)) + flow.value)
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.favoriteDao().deleteTrackEntity(convertFromEntityTrack(track))
        flow_.emit(flow.value.filter { it.trackId != track.trackId })
    }

    override suspend fun getTracks(): StateFlow<List<Track>> {
        val tracks = appDatabase.favoriteDao().getTracks()
        val sortedTracks = tracks.sortedByDescending { it.timestamp }
        flow_.emit(convertFromTrackEntity(sortedTracks))
        return flow
    }

    private fun convertFromTrackEntity(trackEntities: List<FavoriteTrackEntity>): List<Track> {
        return trackEntities.map { entity -> tracksDbConvertor.map(entity) }
    }

    private fun convertFromEntityTrack(track: Track): FavoriteTrackEntity {
        return tracksDbConvertor.map(track)

    }
}