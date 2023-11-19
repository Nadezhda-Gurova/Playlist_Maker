package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface SearchTrackHistoryRepository {

    fun addTrack(track: Track)

    fun getTracks(): List<Track>

    fun clear()
}