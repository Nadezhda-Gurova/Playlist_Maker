package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.NetworkResponse

interface ITunesNetworkClient {
    fun getTracks(track: String): NetworkResponse
}