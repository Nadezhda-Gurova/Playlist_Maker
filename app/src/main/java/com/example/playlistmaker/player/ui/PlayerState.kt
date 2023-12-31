package com.example.playlistmaker.player.ui

import com.example.playlistmaker.search.domain.models.Track

//sealed class UiState {
//    object AddedToFavorites : UiState()
//    object RemovedFromFavorites : UiState()
//    object AddedToPlaylist : UiState()
//    object RemovedFromPlaylist : UiState()
//
//    class CurrentTrack(val track: Track) : UiState()
//    class Progress(val time: String) : UiState()
//    object Prepared : UiState()
//    object Completed : UiState()
//    object ShowPlaying : UiState()
//    object PausePlaying : UiState()
//
//}

class UiState ( val isAddedToFavorites: Boolean,
                val isAddedToPlaylist: Boolean,
                val curTrack: Track,
                val curTime: String,
                val isReady: Boolean,
                val isPausePlaying: Boolean )