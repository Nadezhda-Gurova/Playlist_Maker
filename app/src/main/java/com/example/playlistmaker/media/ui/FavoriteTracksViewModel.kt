package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteInteractor: FavoriteInteractor,
) : ViewModel() {
    private val _favoriteTracksLoadingState = MutableLiveData<FavoriteTracksState>()

    val favoriteTracksLoadingState: LiveData<FavoriteTracksState> = _favoriteTracksLoadingState

    init {
        viewModelScope.launch {
            favoriteInteractor.getTracks().collectLatest { track ->
                processResult(track)
            }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
           renderState(FavoriteTracksState.Empty(""))
        } else {
            renderState(FavoriteTracksState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        _favoriteTracksLoadingState.postValue(state)
    }


     fun addTrack(track: Track) {
        viewModelScope.launch {  favoriteInteractor.addTrack(track) }

    }

     fun deleteTrack(track: Track) {
        viewModelScope.launch {  favoriteInteractor.deleteTrack(track)}
    }
}
