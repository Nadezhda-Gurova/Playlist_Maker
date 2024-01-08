package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.domain.models.Track

class MediaPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val timerInteractor: PlayerRepository
) : ViewModel() {
    private var addedToFavorites = false
    private var addedToPlaylist = false
    private lateinit var curTrack:Track
    private var _uiStateLiveData = MutableLiveData<UiState>()
    val uiStateLiveData: LiveData<UiState> get() = _uiStateLiveData

    private var playerState: PlayerState = PlayerState.NotInited

    fun loadTrackData(track: Track) {
        curTrack = track
        mediaPlayerInteractor.prepare(track.previewUrl,
            onPrepared = {
                playerState = PlayerState.Inited
                _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                    isReady = false,
                )
            },
            onCompletion = {
                playerState = PlayerState.Inited
                _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                    isReady = true,
                )
            })
        _uiStateLiveData.value = UiState(
            isAddedToFavorites = addedToFavorites,
            isAddedToPlaylist = addedToPlaylist,
            curTrack = track,
            curTime = timerInteractor.getCurrentTime(),
            isReady = false,
            isPausePlaying = true
        )
    }

    fun onPlayerClicked() {
        when (playerState) {
            PlayerState.Playing -> {
                pausePlayer()
            }

            PlayerState.Inited, PlayerState.Paused -> {
                startPlayer()
            }

            PlayerState.NotInited -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.play()
        timerInteractor.startProgressUpdate {
            _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                curTime = it,
                isPausePlaying = playerState != PlayerState.Playing
            )
        }
        playerState = PlayerState.Playing
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            curTime = timerInteractor.getCurrentTime(),
            isPausePlaying = playerState != PlayerState.Playing
        )
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pause()
        timerInteractor.pauseProgressUpdate()
        playerState = PlayerState.Paused
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isPausePlaying = playerState != PlayerState.Playing
        )
    }

    fun onDestroy() {
        mediaPlayerInteractor.release()
        timerInteractor.pauseProgressUpdate()
    }

    fun onPause() {
        mediaPlayerInteractor.pause()
    }


    fun addToFavorites() {
        addedToFavorites = !addedToFavorites
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToFavorites = addedToFavorites
        )
    }


    fun addToPlaylist() {
        addedToPlaylist = !addedToPlaylist
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToPlaylist = addedToPlaylist
        )
    }

    companion object {
        fun getViewModelFactory(
            mediaPlayerInteractor: MediaPlayerInteractor,
            playerRepositoryImpl: PlayerRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaPlayerViewModel(
                        mediaPlayerInteractor,
                        playerRepositoryImpl
                    )
                }
            }
    }
}

private sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}