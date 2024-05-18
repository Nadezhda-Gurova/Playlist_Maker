package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerInteractor {
    suspend fun createPlaylist(name: String, description: String, imagePath: String)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>)
    suspend fun getAllPlaylists(): StateFlow<List<PlaylistEntity>>
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity?
    suspend fun deletePlaylistById(playlistId: Int)
}
