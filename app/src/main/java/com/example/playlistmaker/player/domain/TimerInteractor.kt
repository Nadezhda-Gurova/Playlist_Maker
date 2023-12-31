package com.example.playlistmaker.player.domain

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import com.example.playlistmaker.R
import java.text.SimpleDateFormat

interface TimerInteractor {
    fun startProgressUpdate(onUpdate: (String) -> Unit)
    fun pauseProgressUpdate()
    val curTime: String
}

class TimerInteractorImpl(
    private val player: MediaPlayer,
    private val handler: Handler,
    private val format: SimpleDateFormat,
    context: Context
) : TimerInteractor {

    private val zeroTime = context.getString(R.string.zero_time)

    override var curTime: String = zeroTime

    override fun pauseProgressUpdate() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun startProgressUpdate(onUpdate: (String) -> Unit) {
        handler.post(object : Runnable {
            override fun run() {
                if (player.isPlaying) {
                    curTime = format.format(player.currentPosition)
                    onUpdate(curTime)
                    handler.postDelayed(this, 300L)
                } else {
                    curTime = zeroTime
                    onUpdate(zeroTime)
                }
            }
        })
    }
}