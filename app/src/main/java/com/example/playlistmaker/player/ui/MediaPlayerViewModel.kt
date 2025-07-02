package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MediaPlayerViewModel(
    val zeroTime: String,
    private val simpleDateFormat: SimpleDateFormat,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistMakerInteractor: PlaylistMakerInteractor
) : ViewModel() {

    private var curTime: String = zeroTime
    private var onUpdateListener: ((String) -> Unit)? = null
    private lateinit var curTrack: Track
    private var _uiStateLiveData = MutableLiveData<UiState>()
    private var timerJob: Job? = null

    private val player = MediaPlayer()
    val uiStateLiveData: LiveData<UiState> get() = _uiStateLiveData

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _addTrackStatus = MutableLiveData<AddTrackStatus>()
    val addTrackStatus: LiveData<AddTrackStatus> get() = _addTrackStatus

    private var playerState: PlayerState = PlayerState.NotInited

    init {
        viewModelScope.launch {
            playlistMakerInteractor.getAllPlaylists()
            playlistMakerInteractor.state.collectLatest { playlists ->
                _playlists.postValue(playlists)
            }
        }
    }

    fun loadTrackData(track: Track) {
        curTrack = track

        playerPrepare(track.previewUrl)
        _uiStateLiveData.value = UiState(
            isAddedToFavorites = curTrack.favorite,
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
            else -> {}
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
        timerJob?.cancel()
        playerState = PlayerState.Paused
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isPausePlaying = playerState != PlayerState.Playing
        )
    }

    fun onDestroy() {
        player.release()
    }

    fun onPause() {
        player.pause()
    }


    suspend fun onFavoriteClicked() {
        val isAddedToFavorites = _uiStateLiveData.value?.isAddedToFavorites ?: return
        if (!isAddedToFavorites) {
            favoriteInteractor.addTrack(curTrack)

        } else {
            favoriteInteractor.deleteTrack(curTrack)
        }
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToFavorites = !isAddedToFavorites
        )
    }

    fun onPlaylistClicked() {
        val isAddedToPlaylist = _uiStateLiveData.value?.isAddedToPlaylist ?: return
        _uiStateLiveData.value = _uiStateLiveData.value?.copy(
            isAddedToPlaylist = !isAddedToPlaylist
        )
    }

    private fun startProgressUpdate(onUpdate: (String) -> Unit) {
        onUpdateListener = onUpdate
        progressUpdate()
    }


    private fun progressUpdate() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying && this.isActive) {
                curTime = simpleDateFormat.format(player.currentPosition)
                onUpdateListener?.invoke(curTime)
                delay(300L)
            }
        }
    }

    private fun getCurrentTime(): String {
        return curTime
    }

    private fun playerPrepare(url: String) {
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
                curTime = zeroTime,
                isPausePlaying = true
            )
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (playlist.trackIds.contains(track.trackId)) {
            _addTrackStatus.value = AddTrackStatus.AlreadyExists
            return
        }

        viewModelScope.launch {
            try {
                playlistMakerInteractor.addTrackToPlaylist(track, playlist)
                _addTrackStatus.value = AddTrackStatus.Success(playlist)
            } catch (e: Exception) {
                _addTrackStatus.value = AddTrackStatus.Error(e)
            }
        }
    }

    fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
        if (!playlist.trackIds.contains(track.trackId)) {
            _addTrackStatus.value = AddTrackStatus.DoesNotExist
            return
        }

        viewModelScope.launch {
            try {
//                playlistMakerInteractor.removeTrackFromPlaylist(track, playlist)
                _addTrackStatus.value = AddTrackStatus.Removed
            } catch (e: Exception) {
                _addTrackStatus.value = AddTrackStatus.Error(e)
            }
        }
    }

    fun onRestorePlaylists() {
        viewModelScope.launch {
            playlistMakerInteractor.getAllPlaylists()
        }
    }


}

private sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}

sealed class AddTrackStatus {
    data class Success(val playlist: Playlist) : AddTrackStatus()
    data object AlreadyExists : AddTrackStatus()
    data object Removed : AddTrackStatus()
    data object DoesNotExist : AddTrackStatus()
    data class Error(val error: Throwable) : AddTrackStatus()
}