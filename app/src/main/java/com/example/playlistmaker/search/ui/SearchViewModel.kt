package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.interactor.GetTracksListInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.interactor.SearchTrackHistoryInteractor
import com.example.playlistmaker.util.LoadingState

class SearchViewModel(
    private val searchHistory: SearchTrackHistoryInteractor,
    private val listTracks: GetTracksListInteractor
) : ViewModel() {

    fun addTrack(track: Track) {
        TODO("Not yet implemented")
    }

    fun loadHistory() {
        val historyTracks = searchHistory.getTracks()
        _loadingState.value = LoadingState.Success(
            State(historyTracks, isHistory = true)
        )
    }

    fun clearHistory() {
        searchHistory.clear()
    }

    fun searchTrack(query: String) {
        listTracks.execute(query) {
            when (it) {
                is LoadingState.Success -> {
                    _loadingState.postValue(LoadingState.Success(State(it.data, false)))
                }

                is LoadingState.Error -> {
                    _loadingState.postValue(LoadingState.Error(it.message))
                }
            }
        }
    }

    private val _loadingState = MutableLiveData<LoadingState<State>>()
    val loadingState: LiveData<LoadingState<State>> = _loadingState

    companion object {
        fun getViewModelFactory(
            searchHistory: SearchTrackHistoryInteractor,
            listTracks: GetTracksListInteractor
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(
                        searchHistory,
                        listTracks
                    )
                }
            }
    }
}

class State(
    val tracks: List<Track>,
    val isHistory: Boolean
)