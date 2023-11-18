package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchTrackHistoryRepository


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