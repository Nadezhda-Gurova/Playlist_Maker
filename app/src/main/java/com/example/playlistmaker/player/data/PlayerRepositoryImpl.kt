package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import com.example.playlistmaker.player.domain.PlayerRepository
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val handler: Handler,
    private val format: SimpleDateFormat,
    private val zeroTime: String
) : PlayerRepository {

    private var curTime: String = zeroTime
    private var onUpdateListener: ((String) -> Unit)? = null

    override fun startProgressUpdate(onUpdate: (String) -> Unit) {
        onUpdateListener = onUpdate
        handler.postDelayed(progressUpdateRunnable, 300L)
    }

    override fun pauseProgressUpdate() {
        handler.removeCallbacks(progressUpdateRunnable)
    }

    private val progressUpdateRunnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                curTime = format.format(mediaPlayer.currentPosition)
                onUpdateListener?.invoke(curTime)
                handler.postDelayed(this, 300L)
            } else {
                curTime = zeroTime
                onUpdateListener?.invoke(zeroTime)
            }
        }
    }

    override fun getCurrentTime(): String {
        return curTime
    }
}
