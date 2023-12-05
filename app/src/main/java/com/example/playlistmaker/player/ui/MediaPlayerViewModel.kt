package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide.init
import com.example.playlistmaker.search.domain.models.Track

//class MediaPlayerViewModel(
//    private val trackId: String,
//    private val tracksInteractor: TracksInteractor,
//): ViewModel() {
//
//    private var screenStateLiveData = MutableLiveData<TrackScreenState>(TrackScreenState.Loading)
//
//    init {
//        tracksInteractor.loadTrackData(
//            trackId = trackId,
//            onComplete = { trackModel ->
//                // 1
//                screenStateLiveData.postValue(
//                    TrackScreenState.Content(trackModel)
//                )
//            }
//        )
//    }
//
//    fun getScreenStateLiveData(): LiveData<TrackScreenState> = screenStateLiveData
//
//    companion object {
//        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val interactor = (this[APPLICATION_KEY] as MyApplication).provideTracksInteractor()
//
//                MediaPlayerViewModel(
//                    trackId,
//                    interactor,
//                )
//            }
//        }
//    }
//}
