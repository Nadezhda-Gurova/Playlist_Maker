package com.example.playlistmaker.media.ui.playlist.recyclerview

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var trackIds: List<Int>, // This will store the JSON string of track IDs
    var trackCount: Int,
)