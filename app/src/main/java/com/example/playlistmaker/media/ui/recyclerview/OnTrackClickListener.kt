package com.example.playlistmaker.media.ui.recyclerview

import com.example.playlistmaker.search.domain.models.Track

fun interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}