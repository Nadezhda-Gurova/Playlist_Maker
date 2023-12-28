package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TimerInteractor
import com.example.playlistmaker.search.domain.models.Track

class MediaPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val timerInteractor: TimerInteractor
) : ViewModel() {

    private var _playingProgressLiveData = MutableLiveData<String>()
    val playingProgressLiveData: LiveData<String> get() = _playingProgressLiveData

    private var _trackLiveData = MutableLiveData<Track>()
    val trackLiveData: LiveData<Track> get() = _trackLiveData

    private var playerState: PlayerState = PlayerState.NotInited

    fun loadTrackData(track: Track) {
        mediaPlayerInteractor.prepare(track.previewUrl,
            onPrepared = {
                playerState = PlayerState.Inited
                _onPreparedLiveData.value = Unit
            },
            onCompletion = {
                playerState = PlayerState.Inited
                _onCompletionLiveData.value = Unit
            })

        _trackLiveData.value = track
    }

    private var _onPreparedLiveData = MutableLiveData<Unit>()
    val onPreparedLiveData: LiveData<Unit> = _onPreparedLiveData

    private var _onCompletionLiveData = MutableLiveData<Unit>()
    val onCompletionLiveData: LiveData<Unit> = _onCompletionLiveData

    private var _isShowPlayingLiveData = MutableLiveData<Boolean>()
    val isShowPlayingLiveData: LiveData<Boolean> = _isShowPlayingLiveData


    fun onPlayerClicked() {
        when (playerState) {
            PlayerState.Playing -> {
                pausePlayer()
            }

            PlayerState.Inited, PlayerState.Paused -> {
                startPlayer()
            }

            PlayerState.NotInited -> throw IllegalStateException("Player can't be in this state")
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        timerInteractor.startProgressUpdate {
            _playingProgressLiveData.value = it
        }
        playerState = PlayerState.Playing
        _isShowPlayingLiveData.value = false
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pause()
        timerInteractor.pauseProgressUpdate()
        playerState = PlayerState.Paused
        _isShowPlayingLiveData.value = true
    }

    fun onDestroy() {
        mediaPlayerInteractor.release()
        timerInteractor.pauseProgressUpdate()
    }

    fun onPause() {
        mediaPlayerInteractor.pause()
    }

    companion object {
        fun getViewModelFactory(
            mediaPlayerInteractor: MediaPlayerInteractor,
            timerInteractor: TimerInteractor
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaPlayerViewModel(
                        mediaPlayerInteractor,
                        timerInteractor
                    )
                }
            }
    }
}
