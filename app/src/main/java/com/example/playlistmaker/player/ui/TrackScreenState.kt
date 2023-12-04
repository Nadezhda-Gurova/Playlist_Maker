package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track

sealed class TrackScreenState {
    object Loading: TrackScreenState()
    data class Content(
        val track: Track,
    ): TrackScreenState()
}
