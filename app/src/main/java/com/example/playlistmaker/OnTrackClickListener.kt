package com.example.playlistmaker

import com.example.playlistmaker.data.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}