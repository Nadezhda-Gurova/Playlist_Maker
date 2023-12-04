package com.example.playlistmaker.search.ui.recyclerview

import com.example.playlistmaker.search.domain.models.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}