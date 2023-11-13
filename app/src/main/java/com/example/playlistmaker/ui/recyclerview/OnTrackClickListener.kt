package com.example.playlistmaker.ui.recyclerview

import com.example.playlistmaker.data.dto.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}