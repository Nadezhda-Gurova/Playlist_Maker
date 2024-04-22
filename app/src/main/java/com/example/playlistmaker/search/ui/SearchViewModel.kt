package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.util.LoadingState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _loadingState = MutableLiveData<LoadingState<State>>()
    val loadingState: LiveData<LoadingState<State>> = _loadingState

    private var lastQuery: String = ""

    private fun showHistory() {
        val historyTracks = searchHistoryInteractor.getTracks()
        _loadingState.value = LoadingState.Success(
            State(historyTracks, isHistory = true)
        )
    }

    fun addTrack(track: Track) {
        searchHistoryInteractor.addTrack(track)
    }

    fun clearHistory() {
        searchHistoryInteractor.clear()
    }


    fun searchTrack(query: String) {
        lastQuery = query
        if (query.isEmpty()) {
            showHistory()
        } else {
            viewModelScope.launch {
                searchInteractor.execute(query).collect { res ->
                    if (query == lastQuery) {
                        processResult(res)
                    }
                }
            }
        }
    }

    private fun processResult(res: LoadingState<List<Track>>) {

        when (res) {
            is LoadingState.Success -> {
                _loadingState.postValue(LoadingState.Success(State(res.data, false)))
            }

            is LoadingState.Error -> {
                _loadingState.postValue(LoadingState.Error(res.message))
            }
        }
    }
}

class State(
    val tracks: List<Track>,
    val isHistory: Boolean
)