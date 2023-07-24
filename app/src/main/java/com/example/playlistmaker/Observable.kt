package com.example.playlistmaker

import com.example.playlistmaker.data.Track
import java.util.Observer

interface Observable {
    fun add(observer: Observer)
    fun remove(observer: Observer)
    fun notifyObservers(track: Track)
}