package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.search.domain.models.Track

interface MediaTracksRepository {

    fun addTrack(track: Track)

    fun clearTrack(track: Track)

    fun getTracks(): List<Track>

}