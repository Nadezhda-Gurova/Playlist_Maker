package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository


class SearchTrackHistoryInteractor(
    private val historyRepository: SearchTrackHistoryRepository
) {
    fun addTrack(track: Track) {
        historyRepository.addTrack(track)
    }

    fun getTracks(): List<Track> {
        return historyRepository.getTracks()
    }

    fun clear() {
        historyRepository.clear()
    }

}