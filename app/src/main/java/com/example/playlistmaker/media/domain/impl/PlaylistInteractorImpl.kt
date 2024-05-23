package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistMakerRepository) :
    PlaylistMakerInteractor {

    override suspend fun createPlaylist(name: String, description: String, imagePath: String) {
        val playlist = Playlist(
            name = name,
            description = description,
            imagePath = imagePath,
            trackIds = emptyList(),
            trackCount = 0
        )
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun getAllPlaylists(): StateFlow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        playlistRepository.deletePlaylistById(playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.removeTrackFromPlaylist(track, playlist)
    }
}
