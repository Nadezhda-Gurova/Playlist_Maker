package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.activity.SearchActivity.Companion.VIEWED_TRACK_KEY
import com.example.playlistmaker.data.Track
import com.google.gson.Gson

class SearchHistory {

    // чтение
    fun read(sharedPreferences: SharedPreferences): Track? {
        val json = sharedPreferences.getString(VIEWED_TRACK_KEY, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }

    // запись
    fun write(sharedPreferences: SharedPreferences, track: Track) {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(VIEWED_TRACK_KEY, json)
            .apply()
    }
}