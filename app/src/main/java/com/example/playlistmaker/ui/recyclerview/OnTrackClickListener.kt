package com.example.playlistmaker.ui.recyclerview

import com.example.playlistmaker.domain.models.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}