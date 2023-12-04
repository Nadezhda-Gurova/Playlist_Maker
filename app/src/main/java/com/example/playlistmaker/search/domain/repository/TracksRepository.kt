package com.example.playlistmaker.search.domain.repository


import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.LoadingState

interface TracksRepository {
    fun getTracks(track: String): LoadingState<List<Track>>
}
