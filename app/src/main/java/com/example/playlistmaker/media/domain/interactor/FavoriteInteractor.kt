package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface FavoriteInteractor {
    suspend fun getTracks(): StateFlow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}