package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.activity.SearchActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchTrackHistoryImplementation(
    private val sharedPreferences: SharedPreferences
) : SearchTrackHistory {
    override fun addTrack(track: Track) {
        val tracks = getTracks().toMutableList()
        var indexOfElement = -1
        for (i in tracks.indices) {
            if (tracks[i].trackId == track.trackId) {
                indexOfElement = i
            }
        }

        if (indexOfElement != -1) {
            tracks.removeAt(indexOfElement)
        }

        tracks.add(0, track)
        if (tracks.size > 10) {
            tracks.subList(0, 10)
        }

        sharedPreferences.edit().putString(SearchActivity.VIEWED_TRACKS, createJsonFromTracks(tracks)).apply()
    }

    override fun getTracks(): List<Track> {
        return createTracksFromJson(sharedPreferences.getString(SearchActivity.VIEWED_TRACKS, null))
    }

    private fun createJsonFromTracks(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String?): List<Track> {
        if (json == null) {
            return emptyList()
        }
        val item = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, item)
    }
}

