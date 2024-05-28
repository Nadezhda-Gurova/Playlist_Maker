package com.example.playlistmaker.media.ui.favorite

import com.example.playlistmaker.search.domain.models.Track


sealed interface FavoriteTracksState {
    data class Content(
        val tracks: List<Track>
    ) : FavoriteTracksState

    data class Empty(
        val message: String
    ) : FavoriteTracksState
}