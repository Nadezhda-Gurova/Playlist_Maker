package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerInteractor {
    val state : StateFlow<List<Playlist>>
    suspend fun createPlaylist(name: String, description: String, imagePath: String)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getAllPlaylists()
    suspend fun deletePlaylistById(playlistId: Int)
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
//    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)
    suspend fun invalidateState()
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun getTracksByIds(trackIds: List<Int>): List<Track>
    suspend fun deleteTrackFromPlaylist(playlist: Playlist, track: Track)
}
