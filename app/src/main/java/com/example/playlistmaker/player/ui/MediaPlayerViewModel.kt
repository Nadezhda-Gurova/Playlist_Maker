package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.models.Track

class MediaPlayerViewModel(
    zeroTime: String
) : ViewModel() {

    private var curTime: String = zeroTime
    private var onUpdateListener: ((String) -> Unit)? = null
    private var format =  Creator.provideSimpleDateFormat()
    private lateinit var curTrack:Track
    private var _uiStateLiveData = MutableLiveData<UiState>()

    private val handler = Handler(Looper.getMainLooper())

    private val player = MediaPlayer()
    val uiStateLiveData: LiveData<UiState> get() = _uiStateLiveData

    private var playerState: PlayerState = PlayerState.NotInited

    fun loadTrackData(track: Track) {
        curTrack = track

        playerPrepare(track.previewUrl)

        _uiStateLiveData.value = UiState(
            isAddedToFavorites = false,
            isAddedToPlaylist = false,
            curTrack = track,
            curTime = getCurrentTime(),
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
        player.start()

        startProgressUpdate {
            _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                curTime = it,
                isPausePlaying = playerState != PlayerState.Playing
            )
        }

        playerState = PlayerState.Playing
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            curTime = getCurrentTime(),
            isPausePlaying = playerState != PlayerState.Playing
        )
    }

    private fun pausePlayer() {
        player.pause()
        pauseProgressUpdate()
        playerState = PlayerState.Paused
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isPausePlaying = playerState != PlayerState.Playing
        )
    }

    fun onDestroy() {
        player.release()
        pauseProgressUpdate()
    }

    fun onPause() {
        player.pause()
    }


    fun addToFavorites() {
        val isAddedToFavorites = _uiStateLiveData.value?.isAddedToFavorites ?: return
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToFavorites = !isAddedToFavorites
        )
    }


    fun addToPlaylist() {
        val isAddedToPlaylist = _uiStateLiveData.value?.isAddedToPlaylist ?: return
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToPlaylist = !isAddedToPlaylist
        )
    }

    fun startProgressUpdate(onUpdate: (String) -> Unit) {
        onUpdateListener = onUpdate
        handler.postDelayed(progressUpdateRunnable, 300L)
    }

    fun pauseProgressUpdate() {
        handler.removeCallbacks(progressUpdateRunnable)
    }

    private val progressUpdateRunnable = object : Runnable {
        override fun run() {
            if (player.isPlaying) {
                curTime = format.format(player.currentPosition)
                onUpdateListener?.invoke(curTime)
                handler.postDelayed(this, 300L)
            } else {
                curTime = zeroTime
                onUpdateListener?.invoke(zeroTime)
            }
        }
    }

    fun getCurrentTime(): String {
        return curTime
    }

    companion object {
        fun getViewModelFactory(
            zeroTime: String
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MediaPlayerViewModel(zeroTime)
                }
            }
    }

    fun playerPrepare (url: String){
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = PlayerState.Inited
            _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                isReady = false,
            )
        }
        player.setOnCompletionListener {
            playerState = PlayerState.Inited
            _uiStateLiveData.value = _uiStateLiveData.value?.copy(
                isReady = true,
            )
        }
    }

}

private sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}