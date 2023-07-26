package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.activity.SearchActivity.Companion.VIEWED_TRACK
import com.example.playlistmaker.activity.SearchActivity.Companion.VIEWED_TRACKS
import com.example.playlistmaker.data.Track
import com.google.gson.Gson

class SearchHistory (private val sharedPreferences: SharedPreferences) {
    private val history = arrayListOf<Track>()
    fun readTrack(): Track? {
        val json = sharedPreferences.getString(VIEWED_TRACK, null) ?: return null
        return Gson().fromJson(json, Track::class.java)
    }

    fun readTracks(): Array<Track> {
        val json = sharedPreferences.getString(VIEWED_TRACKS, null) ?: return arrayOf<Track>()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun writeTrack(track: Track) {
        val json = Gson().toJson(track)

        sharedPreferences.edit()
                .putString(VIEWED_TRACK, json)
                .apply()
    }

    fun writeTracks(track: Track?) {
        if(track != null) {
            var tracksJson = sharedPreferences.getString(VIEWED_TRACKS, null)

            tracksJson = tracksJson ?: "[]"
            val tracks = createTracksFromJson(tracksJson)

            history.addAll(tracks)

            val index = history.indexOf(track)

            if (history.size <= 10) {
                if (index != -1) {
                    history.remove(track)
                    history.add(0, track)
                } else {
                    history.add(track)
                }
            }

            sharedPreferences.edit().putString(
                VIEWED_TRACKS, createJsonFromTracks(
                    history
                )
            ).apply()
            history.clear()
        }
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }
}