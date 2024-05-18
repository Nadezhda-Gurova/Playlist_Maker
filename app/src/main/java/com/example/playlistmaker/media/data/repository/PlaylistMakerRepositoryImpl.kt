package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.db.PlaylistsAppDatabase
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaylistMakerRepositoryImpl(private val appDatabase: PlaylistsAppDatabase) :
    PlaylistMakerRepository {

    private var _flow = MutableStateFlow<List<PlaylistEntity>>(emptyList())
    private val flow: StateFlow<List<PlaylistEntity>> = _flow

    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        appDatabase.playlistsDao().insert(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        appDatabase.playlistsDao().update(playlist)
    }

    override suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>) {
        val trackIdsJson = Gson().toJson(trackIds)
        appDatabase.playlistsDao().updateTrackIds(playlistId = playlistId, trackIds = trackIdsJson)
    }

    override suspend fun getAllPlaylists(): StateFlow<List<PlaylistEntity>> {
        val tracks = appDatabase.playlistsDao().getAllPlaylists()
        val sortedPlaylists = tracks.sortedByDescending { it.timestamp }
        _flow.emit(sortedPlaylists)
        return flow
    }

    override suspend fun getPlaylistById(playlistId: Int): PlaylistEntity? {
        return  appDatabase.playlistsDao().getPlaylistById(playlistId)
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
       appDatabase.playlistsDao().deletePlaylistById(playlistId)
    }
}
