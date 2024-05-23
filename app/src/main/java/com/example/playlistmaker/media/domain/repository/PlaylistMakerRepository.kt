package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists(): StateFlow<List<Playlist>>
    suspend fun deletePlaylistById(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)
}
