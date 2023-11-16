package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.domain.models.Resource
import com.example.playlistmaker.domain.repository.TracksRepository

class ITunesRepositoryImpl(private val itunesNetworkClient: ITunesNetworkClient): TracksRepository {
    override fun getTracksItunes(track:String): Resource<ITunesResponse> {
        val itunesResponse = itunesNetworkClient.getTracks(track)
        return if (itunesResponse is ITunesResponse) {
            Resource.Success(itunesResponse)
        } else {
            Resource.Error("Произошла сетевая ошибка")
        }
    }

}