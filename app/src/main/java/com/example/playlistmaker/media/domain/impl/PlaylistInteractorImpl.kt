package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistMakerRepository) :
    PlaylistMakerInteractor {
    override val state: StateFlow<List<Playlist>> = playlistRepository.state

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

    override suspend fun getAllPlaylists() {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun invalidateState() {
        playlistRepository.invalidateState()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun getTracksByIds(trackIds: List<Int>): List<Track> {
        return playlistRepository.getTracksByIds(trackIds)
    }

//    override suspend fun getAllTracks(): List<Track> {
//        return playlistRepository.getAllTracks()
//    }

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
