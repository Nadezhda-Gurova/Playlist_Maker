package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.converters.TracksDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImp(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: TracksDbConvertor,
) : FavoriteTracksRepository {

    override suspend fun addTrack(track: Track) {
        val curTime = System.currentTimeMillis()
        val entity = convertFromEntityTrack(track)
        appDatabase.favoriteDao().insertTrack(entity.copy(timestamp = curTime))
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.favoriteDao().deleteTrackEntity(convertFromEntityTrack(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoriteDao().getTracks()
        val sortedTracks = tracks.sortedByDescending { it.timestamp }
        emit(convertFromTrackEntity(sortedTracks))
    }

    private fun convertFromTrackEntity(trackEntities: List<FavoriteTrackEntity>): List<Track> {
        return trackEntities.map { entity -> movieDbConvertor.map(entity) }
    }

    private fun convertFromEntityTrack(track: Track): FavoriteTrackEntity {
        return movieDbConvertor.map(track)
    }
}