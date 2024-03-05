package com.example.playlistmaker.media.domain.interactor

import com.example.playlistmaker.media.domain.repository.MediaTracksRepository
import com.example.playlistmaker.search.domain.models.Track

class MediaTracksInteractor(private val mediaRepository: MediaTracksRepository) {
    fun addTrack(track: Track){
        mediaRepository.addTrack(track)
    }

    fun clearTrack(track: Track){
        mediaRepository.clearTrack(track)
    }

    fun getTracks(): List<Track>{
        return mediaRepository.getTracks()
    }
}