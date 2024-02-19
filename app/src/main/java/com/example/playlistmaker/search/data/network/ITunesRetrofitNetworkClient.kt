package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.NetworkResponse
import com.example.playlistmaker.search.data.repository.ITunesNetworkClient

class ITunesRetrofitNetworkClient : ITunesNetworkClient {
    override fun getTracks(track: String): NetworkResponse {
        return try {
            val response = RetrofitClient.api.search(track).execute()
            val networkResponse = response.body() ?: NetworkResponse()

            networkResponse.apply { resultCode = response.code() }
        } catch (ex: Exception) {
            NetworkResponse().apply { resultCode = 400 }
        }
    }
}