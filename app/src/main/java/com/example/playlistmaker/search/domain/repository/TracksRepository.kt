package com.example.playlistmaker.search.domain.repository


import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.LoadingState
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun getTracks(track: String): Flow<LoadingState<List<Track>>>
}
