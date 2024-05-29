package com.example.playlistmaker.media.ui.playlist_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.data.dto.TrackTime
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistMakerInteractor
) :
    ViewModel() {
    private val _uiStateLiveData = MutableLiveData<PlaylistUIState>()
    val uiStateLiveData: LiveData<PlaylistUIState> get() = _uiStateLiveData

    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = _tracksLiveData

    private lateinit var currentPlaylist: Playlist

    fun loadPlaylist(playlistId: Int) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId)
            currentPlaylist = playlist
            val tracks = playlistInteractor.getTracksByIds(playlist.trackIds)
            val trackTimes = tracks.map { TrackTime(it.trackTime) }
            val totalDuration = calculateTotalDuration(trackTimes)
            val uiState = setPlaylistData(playlist, totalDuration)
            _uiStateLiveData.postValue(uiState)
            _tracksLiveData.postValue(tracks)
        }
    }

    private fun setPlaylistData(playlist: Playlist, totalDuration: String): PlaylistUIState {
        val uiState = PlaylistUIState(
            name = playlist.name,
            description = playlist.description,
            imagePath = playlist.imagePath,
            trackCount = playlist.trackCount,
            totalDuration = totalDuration
        )
        _uiStateLiveData.value = uiState
        return uiState
    }

    private fun calculateTotalDuration(tracks: List<TrackTime>): String {
        val totalDurationMillis = tracks.sumOf { parseTimeToMillis(it.time) }
        val totalMinutes = totalDurationMillis / 60000
        return totalMinutes.toString()
    }

    private fun parseTimeToMillis(timeString: String): Long {
        val parts = timeString.split(":")
        val minutes = parts[0].toLong() * 60000
        val seconds = parts[1].toLong() * 1000
        return minutes + seconds
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(currentPlaylist, track)
            val updatedTracks = playlistInteractor.getTracksByIds(currentPlaylist.trackIds)
            _tracksLiveData.postValue(updatedTracks)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistById(playlistId)
        }
    }

}

data class PlaylistUIState(
    val name: String,
    val description: String,
    val imagePath: String,
    val trackCount: Int,
    val totalDuration: String
)