package com.example.playlistmaker.media.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.media.domain.repository.MediaTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.util.VIEWED_TRACKS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MediaTracksRepositoryImpl(private val sharedPreferences: SharedPreferences,) :
    MediaTracksRepository {
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

            sharedPreferences.edit().putString(SAVED_TRACKS, createJsonFromTracks(tracks)).apply()
    }

    override fun clearTrack(track: Track) {
        val tracks = getTracks().toMutableList()
        tracks.remove(track)
        sharedPreferences.edit().putString(SAVED_TRACKS, createJsonFromTracks(tracks)).apply()
    }

    override fun getTracks(): List<Track> {
        return createTracksFromJson(sharedPreferences.getString(SAVED_TRACKS, null))
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

    companion object {
    const val SAVED_TRACKS = "key_for_saved_tracks"}
}

