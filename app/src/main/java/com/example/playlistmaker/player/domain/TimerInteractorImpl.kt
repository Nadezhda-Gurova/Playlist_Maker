package com.example.playlistmaker.player.domain

class TimerInteractorImpl(
//    private val player: MediaPlayer,
//    private val handler: Handler,
//    private val format: SimpleDateFormat,
//    zeroTime: String
    private val playerRepository: PlayerRepository
) : TimerInteractor {

//    private val time = zeroTime

    override var curTime: String = ""

    override fun pauseProgressUpdate() {
//        handler.removeCallbacksAndMessages(null)
        playerRepository.pauseProgressUpdate()
     }

    override fun startProgressUpdate(onUpdate: (String) -> Unit) {
//        handler.post(object : Runnable {
//            override fun run() {
//                if (player.isPlaying) {
//                    curTime = format.format(player.currentPosition)
//                    onUpdate(curTime)
//                    handler.postDelayed(this, 300L)
//                } else {
//                    curTime = time
//                    onUpdate(time)
//                }
//            }
//        })
        playerRepository.startProgressUpdate {
            curTime = it
            onUpdate(it)
        }
    }
}