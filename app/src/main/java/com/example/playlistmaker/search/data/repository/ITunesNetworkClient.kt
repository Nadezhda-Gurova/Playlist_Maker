package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.NetworkResponse

interface ITunesNetworkClient {
    suspend fun getTracks(track: String): NetworkResponse
}