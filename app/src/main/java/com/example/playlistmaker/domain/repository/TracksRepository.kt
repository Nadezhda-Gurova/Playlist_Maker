package com.example.playlistmaker.domain.repository


import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.domain.models.Resource

interface TracksRepository {
    fun getTracksItunes(track: String): Resource<ITunesResponse>
}
