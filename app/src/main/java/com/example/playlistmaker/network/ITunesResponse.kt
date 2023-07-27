package com.example.playlistmaker.network

import com.example.playlistmaker.data.Track

class ITunesResponse(
    val resultCount: Int,
    val results: List<Track>,
)