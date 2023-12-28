package com.example.playlistmaker.player.ui

sealed class PlayerState {
    object NotInited : PlayerState()
    object Inited : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
}