package com.example.playlistmaker.player.ui

import android.media.MediaPlayer

//class MediaPlayerInteractorImpl(
//    private val mediaPlayer: MediaPlayer
//) : MediaPlayerInteractor {
//
//    private var playerState: PlayerState = PlayerState.NotInited
//
//    override fun prepare(url: String, onPrepared: () -> Unit,  onCompletion: () -> Unit) {
//        mediaPlayer.setDataSource(url)
//        mediaPlayer.prepareAsync()
//        mediaPlayer.setOnPreparedListener {
//            playerState = PlayerState.Inited
//            onPrepared()
//        }
//        mediaPlayer.setOnCompletionListener {
//            playerState = PlayerState.Inited
//            onCompletion()
//        }
//    }
//
//    private fun onCreate() {
//        //prepare(""){
//            //binding.playButton.isEnabled = true
//        }
//
//
//    override fun release() {
//        TODO("Not yet implemented")
//    }
//
//    override fun play() {
//        TODO("Not yet implemented")
//    }
//
//    override fun pause() {
//        TODO("Not yet implemented")
//    }
//}