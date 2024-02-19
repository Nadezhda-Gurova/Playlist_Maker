package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.models.Track

interface SearchTrackHistoryRepository {

    fun addTrack(track: Track)

    fun getTracks(): List<Track>

    fun clear()
}