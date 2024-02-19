package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.interactor.SearchInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.interactor.SearchHistoryInteractor
import com.example.playlistmaker.util.LoadingState

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
            searchInteractor.execute(query) {
                if (query == lastQuery) {
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
        }
    }

//    companion object {
//        fun getViewModelFactory(
//            searchHistory: SearchHistoryInteractor,
//            listTracks: SearchInteractor
//        ): ViewModelProvider.Factory =
//            viewModelFactory {
//                initializer {
//                    SearchViewModel(
//                        searchHistory,
//                        listTracks
//                    )
//                }
//            }
//    }
}

class State(
    val tracks: List<Track>,
    val isHistory: Boolean
)