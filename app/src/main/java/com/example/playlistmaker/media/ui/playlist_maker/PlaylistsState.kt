package com.example.playlistmaker.media.ui.playlist_maker

import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist

sealed interface PlaylistsState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data class Empty(
        val message: String
    ) : PlaylistsState
}