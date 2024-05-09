package com.example.playlistmaker.media.domain.repository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface FavoriteTracksRepository {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    suspend fun getTracks(): StateFlow<List<Track>>
}