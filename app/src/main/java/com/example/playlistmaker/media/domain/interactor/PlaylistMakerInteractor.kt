package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import kotlinx.coroutines.flow.StateFlow

interface PlaylistMakerInteractor {
    suspend fun createPlaylist(name: String, description: String, imagePath: String)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>)
    suspend fun getAllPlaylists(): StateFlow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int): Playlist?
    suspend fun deletePlaylistById(playlistId: Int)
}
