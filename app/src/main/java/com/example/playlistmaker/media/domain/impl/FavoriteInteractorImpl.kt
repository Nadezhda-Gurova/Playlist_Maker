package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.media.domain.repository.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteTracksRepository): FavoriteInteractor {
    override fun getTracks(): Flow<List<Track>> {
       return favoriteRepository.getTracks()
    }

    override suspend fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }
}