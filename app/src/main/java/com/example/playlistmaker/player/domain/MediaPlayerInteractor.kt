package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {
    fun release()
    fun play()
    fun pause()
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
}