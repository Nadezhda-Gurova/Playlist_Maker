package com.example.playlistmaker.domain.repository


import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.LoadingState

interface TracksRepository {
    fun getTracks(track: String): LoadingState<List<Track>>
}
