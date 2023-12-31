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

    private var _uiStateLiveData = MutableLiveData<UiState>()
    val uiStateLiveData: LiveData<UiState> get() = _uiStateLiveData

    private var playerState: PlayerState = PlayerState.NotInited

    fun loadTrackData(track: Track) {
        mediaPlayerInteractor.prepare(track.previewUrl,
            onPrepared = {
                playerState = PlayerState.Inited
                _uiStateLiveData.value = UiState.Prepared
            },
            onCompletion = {
                playerState = PlayerState.Inited
                _uiStateLiveData.value = UiState.Completed
            })

        _uiStateLiveData.value = UiState.CurrentTrack(track)
    }

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
            _uiStateLiveData.value = UiState.Progress(it)
        }
        playerState = PlayerState.Playing
        _uiStateLiveData.value = UiState.PausePlaying
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pause()
        timerInteractor.pauseProgressUpdate()
        playerState = PlayerState.Paused
        _uiStateLiveData.value = UiState.ShowPlaying
    }

    fun onDestroy() {
        mediaPlayerInteractor.release()
        timerInteractor.pauseProgressUpdate()
    }

    fun onPause() {
        mediaPlayerInteractor.pause()
    }

    private var isAddToFavoritesClicked = false

    fun addToFavorites() {
        isAddToFavoritesClicked = if (isAddToFavoritesClicked) {
            _uiStateLiveData.value = UiState.RemovedFromFavorites
            false
        } else {
            _uiStateLiveData.value = UiState.AddedToFavorites
            true
        }
    }

    private var isAddToPlaylistClicked = false

    fun addToPlaylist() {
        isAddToPlaylistClicked = if (isAddToPlaylistClicked) {
            _uiStateLiveData.value = UiState.AddedToPlaylist
            false
        } else {
            _uiStateLiveData.value = UiState.RemovedFromPlaylist
            true
        }
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

private sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}