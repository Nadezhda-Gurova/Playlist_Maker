package com.example.playlistmaker.media.ui.playlist_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.media.ui.playlist_maker.PlaylistMakerViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistInteractor: PlaylistMakerInteractor) :
    PlaylistMakerViewModel(playlistInteractor) {

    private val _playlistEdit = MutableLiveData<Playlist>()
    val playlistEdit: LiveData<Playlist> get() = _playlistEdit

    fun editPlaylist(playlistId: Int, name: String, description: String, imagePath: String) {
        viewModelScope.launch {
            val playlist =  playlistInteractor.editPlaylist(playlistId, name, description, imagePath)
            _playlistEdit.postValue(playlist)
        }
    }

    fun loadPlaylist(playlistId: Int) {
        viewModelScope.launch {
            try {
                val playlist = playlistInteractor.getPlaylistById(playlistId)
                _playlistEdit.postValue(playlist)
            } catch (e: Exception) {
                // Обработка ошибок, например, отображение сообщения об ошибке
            }
        }
    }
}
