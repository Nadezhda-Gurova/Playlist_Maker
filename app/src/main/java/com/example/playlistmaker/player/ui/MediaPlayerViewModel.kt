package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerViewModel(
    //private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

//    private var _trackLiveData = MutableLiveData<Track>()
//    val trackLiveData: LiveData<Track> get() = _trackLiveData
//
//    private val _playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.NotInited)
//    val playerStateLiveData: LiveData<PlayerState> get() = _playerStateLiveData
//
//
//    private val mediaPlayer = MediaPlayer()
//
//    fun loadTrackData(track: Track) {
//        viewModelScope.launch(Dispatchers.IO) {
//            // Simulate loading data from a repository
//            // Replace with actual data loading logic
//            // For example: val loadedTrack = repository.loadTrack(trackId)
//            val loadedTrack = track
//            _trackLiveData.postValue(loadedTrack)
//        }
//    }
//
//    private var _onPreparedLiveData = MutableLiveData<Unit>()
//    val onPreparedLiveData: LiveData<Unit> = _onPreparedLiveData
//
//    private var _onCompletionLiveData = MutableLiveData<Unit>()
//    val onCompletionLiveData: LiveData<Unit> = _onCompletionLiveData
//
//    fun start(url: String) {
//        mediaPlayerInteractor.prepare(url,
//            onPrepared = {
//                _onPreparedLiveData.value = Unit
//            },
//            onCompletion = {
//                _onCompletionLiveData.value = Unit
//            })
//    }
//
//    fun switchPlayerState() {
//        when (_playerStateLiveData.value) {
//            PlayerState.Playing -> {
//                pausePlayer()
//            }
//
//            PlayerState.Inited, PlayerState.Paused -> {
//                startPlayer()
//            }
//
//            PlayerState.NotInited -> throw IllegalStateException("Player can't be in this state")
//            else -> {}
//        }
//    }
//
//
//    private fun startPlayer() {
//        // Add logic to start the player
//        mediaPlayer.start()
//        playerState = PlayerState.Playing
//        //startProgressUpdate()
//        _playerStateLiveData.postValue(PlayerState.Playing)
//    }
//
//    private fun pausePlayer() {
//        // Add logic to pause the player
//        mediaPlayer.pause()
//        playerState = PlayerState.Paused
//        //pauseProgressUpdate()
//        _playerStateLiveData.postValue(PlayerState.Paused)
//    }
//
//    private fun startProgressUpdate() {
//        if (mediaPlayer.isPlaying) {
//            Log.d("TIME_IN_PLAYER", "${mediaPlayer.currentPosition}")
//            binding.time.text = SimpleDateFormat(
//                "mm:ss",
//                Locale.getDefault()
//            ).format(mediaPlayer.currentPosition)
//            handler.postDelayed(this, 300L)
//        } else {
//            binding.time.text = getString(R.string.zero_time)
//        }
//    }


}
