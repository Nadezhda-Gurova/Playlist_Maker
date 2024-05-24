package com.example.playlistmaker.media.ui.playlist_maker


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import kotlinx.coroutines.flow.collectLatest

class PlaylistMakerViewModel(private val playlistInteractor: PlaylistMakerInteractor) :
    ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    init {
        viewModelScope.launch {
            playlistInteractor.state.collectLatest { playlists ->
                _playlists.postValue(playlists)
            }
        }
    }

    fun createPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(name, description, imagePath)
        }
    }

    fun onDestroy() {
        viewModelScope.launch {
            playlistInteractor.invalidateState()
        }
    }

    fun onRestore() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
        }
    }
}

