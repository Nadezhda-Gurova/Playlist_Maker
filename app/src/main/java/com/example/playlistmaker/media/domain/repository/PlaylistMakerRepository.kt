package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerRepository {
    val state: StateFlow<List<Playlist>>
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists()
    suspend fun deletePlaylistById(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun invalidateState()
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun getTracksByIds(trackIds: List<Int>): List<Track>
    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)

}
