package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.interactor.MediaTracksInteractor
import com.example.playlistmaker.search.domain.models.Track

class FavoriteTracksViewModel(
    private val mediaTracksInteractor: MediaTracksInteractor,
    ): ViewModel() {
    private val _loadingState = MutableLiveData<List<Track>>()
    val loadingState: LiveData<List<Track>> = _loadingState

    fun addTrack(track: Track) {
        mediaTracksInteractor.addTrack(track)
    }

    fun clearHistory(track: Track) {
        mediaTracksInteractor.clearTrack(track)
    }

    fun getTracks(){
        _loadingState.value = mediaTracksInteractor.getTracks()
    }
}
