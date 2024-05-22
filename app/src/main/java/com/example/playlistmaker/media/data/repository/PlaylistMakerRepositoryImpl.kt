package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.converters.PlaylistsDbConvertor
import com.example.playlistmaker.media.data.db.PlaylistsAppDatabase
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaylistMakerRepositoryImpl(
    private val appDatabase: PlaylistsAppDatabase,
    private val playlistsDbConvertor: PlaylistsDbConvertor,
) :
    PlaylistMakerRepository {

    private var _flow = MutableStateFlow<List<Playlist>>(emptyList())
    private val flow: StateFlow<List<Playlist>> = _flow

    override suspend fun insertPlaylist(playlist: Playlist) {
        val entity = convertFromPlaylist(playlist)
        val curTime = System.currentTimeMillis()
        appDatabase.playlistsDao()
            .insert(entity.copy(timestamp = curTime, trackCount = playlist.trackCount))
        _flow.emit(
            listOf(
                playlist.copy(trackCount = playlist.trackCount)
            ) + flow.value
        )
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().update(convertFromPlaylist(playlist))
    }

    override suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>) {
        val trackIdsJson = Gson().toJson(trackIds)
        appDatabase.playlistsDao().updateTrackIds(playlistId = playlistId, trackIds = trackIdsJson)
    }

    override suspend fun getAllPlaylists(): StateFlow<List<Playlist>> {
        val playlists = appDatabase.playlistsDao().getAllPlaylists()
        val sortedPlaylists = playlists.sortedByDescending { it.timestamp }
        _flow.emit(convertFromPlaylistsEntity(sortedPlaylists))
        return flow
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist? {
        return appDatabase.playlistsDao().getPlaylistById(playlistId)
            ?.let { playlistsDbConvertor.map(it) }
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        appDatabase.playlistsDao().deletePlaylistById(playlistId)
    }

    private fun convertFromPlaylistsEntity(playlistEntities: List<PlaylistEntity>): List<Playlist> {
        return playlistEntities.map { entity -> playlistsDbConvertor.map(entity) }
    }

    private fun convertFromPlaylist(playlistEntities: Playlist): PlaylistEntity {
        return playlistsDbConvertor.map(playlistEntities)
    }
}
