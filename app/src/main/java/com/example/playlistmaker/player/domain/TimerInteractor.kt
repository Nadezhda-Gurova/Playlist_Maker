package com.example.playlistmaker.player.domain

interface TimerInteractor {
    fun startProgressUpdate(onUpdate: (String) -> Unit)
    fun pauseProgressUpdate()
    val curTime: String
}