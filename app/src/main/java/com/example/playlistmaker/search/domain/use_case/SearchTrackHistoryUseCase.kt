package com.example.playlistmaker.search.domain.use_case

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository


class SearchTrackHistoryUseCase(
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