package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITunesRetrofitNetworkClient(private val imdbApi: IMDbApi) : ITunesNetworkClient {
    override suspend fun getTracks(track: String): NetworkResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = imdbApi.search(track)
                response.apply { resultCode = 200 }
            } catch (ex: Exception) {
                NetworkResponse().apply { resultCode = 400 }
            }
        }
    }
}