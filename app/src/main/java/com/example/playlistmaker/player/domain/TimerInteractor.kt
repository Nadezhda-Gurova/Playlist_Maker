package com.example.playlistmaker.player.domain

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import com.example.playlistmaker.R
import java.text.SimpleDateFormat

interface TimerInteractor {
    fun startProgressUpdate(onUpdate: (String) -> Unit)
    fun pauseProgressUpdate()
}

class TimerInteractorImpl (
    private val player: MediaPlayer,
    private val handler: Handler,
    private val format: SimpleDateFormat,
    private val context: Context
): TimerInteractor {

    override fun pauseProgressUpdate() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun startProgressUpdate(onUpdate: (String) -> Unit) {
        handler.post(object : Runnable {
            override fun run() {
                if (player.isPlaying) {
                    onUpdate(format.format(player.currentPosition))
                    handler.postDelayed(this, 300L)
                } else {
                    onUpdate(context.getString(R.string.zero_time))
                }
            }
        })
    }

}