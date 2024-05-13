package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteTracksRepository): FavoriteInteractor {
    override suspend fun getTracks(): StateFlow<List<Track>> {
       return favoriteRepository.getTracks()
    }

    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }
}