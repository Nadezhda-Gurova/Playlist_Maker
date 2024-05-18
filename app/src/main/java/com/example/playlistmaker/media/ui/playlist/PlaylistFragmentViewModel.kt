package com.example.playlistmaker.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

class PlaylistFragmentViewModel: ViewModel() {
    private val _loadingState = MutableLiveData<List<Track>>()
    val loadingState: LiveData<List<Track>> = _loadingState
}