package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.StateFlow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistMakerRepository) :
    PlaylistMakerInteractor {

    override suspend fun createPlaylist(name: String, description: String, imagePath: String) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            imagePath = imagePath,
            trackIds = Gson().toJson(emptyList<Int>()),
            trackCount = 0
        )
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistRepository.updatePlaylist(playlist)
    }


    override suspend fun updateTrackIds(playlistId: Int, trackIds: List<Int>) {
        val playlist = playlistRepository.getPlaylistById(playlistId)
        if (playlist != null) {
            playlist.trackIds = Gson().toJson(trackIds)
            playlist.trackCount = trackIds.size
            playlistRepository.updatePlaylist(playlist)
        }
    }

    override suspend fun getAllPlaylists(): StateFlow<List<PlaylistEntity>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): PlaylistEntity? {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        playlistRepository.deletePlaylistById(playlistId)
    }
}
