package com.example.playlistmaker

import com.example.playlistmaker.data.Track

interface SearchTrackHistory {

    fun addTrack(track: Track)

    fun getTracks(): List<Track>

}