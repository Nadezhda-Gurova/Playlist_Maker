package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

class MediaPlayerViewModel: ViewModel()
{

    private val _trackData = MutableLiveData<Track>()
    val trackData: LiveData<Track> get() = _trackData

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    init {
        _playerState.value = PlayerState.NotInited
    }

    fun setTrackData(track: Track) {
        _trackData.value = track
    }

    fun switchPlayerState() {
        when (_playerState.value) {
            PlayerState.Playing -> {
                pausePlayer()
            }

            PlayerState.Inited, PlayerState.Paused -> {
                startPlayer()
            }

            else -> {throw IllegalStateException("Player can't be in this state")}
        }
    }

    // Добавьте другие методы управления плеером и дополнительную логику по мере необходимости.

    private fun startPlayer() {
        // Логика для начала проигрывания
        _playerState.value = PlayerState.Playing
    }

    private fun pausePlayer() {
        // Логика для приостановки проигрывания
        _playerState.value = PlayerState.Paused
    }
}
