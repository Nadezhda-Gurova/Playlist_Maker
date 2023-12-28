package com.example.playlistmaker.player.domain

import android.media.MediaPlayer

class MediaPlayerInteractorImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerInteractor {

    override fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }
}