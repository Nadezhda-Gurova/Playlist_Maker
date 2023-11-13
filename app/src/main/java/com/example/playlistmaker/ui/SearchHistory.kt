package com.example.playlistmaker.ui

import com.example.playlistmaker.data.dto.Track

interface SearchTrackHistoryRepository {

    fun addTrack(track: Track)

    fun getTracks(): List<Track>

}