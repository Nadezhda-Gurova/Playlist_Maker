package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track


data class UiState(
    val isAddedToFavorites: Boolean,
    val isAddedToPlaylist: Boolean,
    val curTrack: Track,
    val curTime: String,
    val isReady: Boolean,
    val isPausePlaying: Boolean
)