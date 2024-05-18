package com.example.playlistmaker.media.ui.media.recyclerview

import com.example.playlistmaker.search.domain.models.Track

fun interface OnPlaylistsClickListener {
    fun onPlaylistClick(playlist: PLaylists)
}