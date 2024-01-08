package com.example.playlistmaker.player.domain

interface PlayerRepository {
    fun startProgressUpdate(onUpdate: (String) -> Unit)
    fun pauseProgressUpdate()
    fun getCurrentTime(): String
}
