package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerRepository {
    suspend fun insertPlaylist(playlist: PlaylistEntity)
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>)
    suspend fun getAllPlaylists(): StateFlow<List<PlaylistEntity>>
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity?
    suspend fun deletePlaylistById(playlistId: Int)
}
