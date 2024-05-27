package com.example.playlistmaker.media.ui.playlist_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlistInteractor: PlaylistMakerInteractor
) :
    ViewModel() {
    private val _uiStateLiveData = MutableLiveData<PlaylistUIState>()
    val uiStateLiveData: LiveData<PlaylistUIState> get() = _uiStateLiveData

    fun loadPlaylist(playlistId: Int) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId)
            val tracks = playlistInteractor.getTracksByIds(playlist.trackIds)
            val uiState = setPlaylistData(playlist)
            _uiStateLiveData.postValue(uiState)
        }
    }

    private fun setPlaylistData(playlist: Playlist): PlaylistUIState {
        val totalDuration = calculateTotalDuration(playlist.trackIds)
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

    private fun calculateTotalDuration(tracks: List<Int>): String {
//        val totalDurationMillis = tracks.sumOf { it.trackTimeMillis }
//        val totalMinutes = totalDurationMillis / 60000
//        return SimpleDateFormat("mm", Locale.getDefault()).format(totalMinutes * 60000)
        return ""
    }

}

data class PlaylistUIState(
    val name: String,
    val description: String,
    val imagePath: String,
    val trackCount: Int,
    val totalDuration: String
)