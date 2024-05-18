package com.example.playlistmaker.media.ui.playlist_maker


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.interactor.PlaylistMakerInteractor
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlaylistMakerViewModel(private val playlistInteractor: PlaylistMakerInteractor) :
    ViewModel() {

    private val _playlists = MutableLiveData<List<PlaylistEntity>>()
    val playlists: LiveData<List<PlaylistEntity>> get() = _playlists


    fun createPlaylist(name: String, description: String, imagePath: String) {
        viewModelScope.launch {

            playlistInteractor.createPlaylist(name, description, imagePath)


        }
    }

//    fun updateTrackIds(playlistId: Int, trackIds: List<Int>) {
//        viewModelScope.launch {
//            try {
//                playlistInteractor.updateTrackIds(playlistId, trackIds)
//            } catch (e: Exception) {
//                _error.value = "Error updating track ids: ${e.message}"
//            }
//        }
//    }

//    private fun loadAllPlaylists() {
//        viewModelScope.launch {
//                _playlists.value = playlistInteractor.getAllPlaylists()
//
//        }
}

//    fun createPlaylist(playlistName: String) {
//        // Логика создания плейлиста
//        // ...
//
//        // Отправка данных о созданном плейлисте в MediaViewModel
//        playlistRepository.onPlaylistCreated(playlistName)
//    }
//}
