package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerInteractor {
    suspend fun createPlaylist(name: String, description: String, imagePath: String)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists(): StateFlow<List<Playlist>>
    suspend fun deletePlaylistById(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)
}
