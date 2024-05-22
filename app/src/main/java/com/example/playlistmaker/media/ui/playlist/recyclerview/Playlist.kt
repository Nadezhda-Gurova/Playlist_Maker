package com.example.playlistmaker.media.ui.playlist.recyclerview

data class Playlist(
    val name: String,
    val description: String,
    val imagePath: String,
    var trackIds: String, // This will store the JSON string of track IDs
    var trackCount: Int,
)