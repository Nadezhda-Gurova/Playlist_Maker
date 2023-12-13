package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
): ViewModel() {

    private var _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> get() = _trackLiveData

    private val _playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.NotInited)
    val playerStateLiveData: LiveData<PlayerState> get() = _playerStateLiveData

    fun loadTrackData(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            // Simulate loading data from a repository
            // Replace with actual data loading logic
            // For example: val loadedTrack = repository.loadTrack(trackId)
            val loadedTrack = track
            _trackLiveData.postValue(loadedTrack)
        }
    }

    fun switchPlayerState() {
        when (_playerStateLiveData.value) {
            PlayerState.Playing -> {
                pausePlayer()
            }

            PlayerState.Inited, PlayerState.Paused -> {
                startPlayer()
            }

            PlayerState.NotInited -> throw IllegalStateException("Player can't be in this state")
            else -> {}
        }
    }

    private fun startPlayer() {
        // Add logic to start the player
        _playerStateLiveData.postValue(PlayerState.Playing)
    }

    private fun pausePlayer() {
        // Add logic to pause the player
        _playerStateLiveData.postValue(PlayerState.Paused)
    }


}
