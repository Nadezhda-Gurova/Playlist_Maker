package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>)
    suspend fun getAllPlaylists(): StateFlow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int): Playlist?
    suspend fun deletePlaylistById(playlistId: Int)
}
