package com.example.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.SearchTrackHistoryRepository
import com.example.playlistmaker.search.domain.util.VIEWED_TRACKS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking

class TrackHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase,
) : SearchTrackHistoryRepository {
    override fun addTrack(track: Track) {
        var tracks = getTracks().toMutableList()
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
        if (tracks.size > LIMIT) {
            tracks = tracks.subList(0, LIMIT)
        }
        sharedPreferences.edit().putString(VIEWED_TRACKS, createJsonFromTracks(tracks)).apply()
    }
    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    override fun getTracks(): List<Track> {
        val favoriteTrackIds = runBlocking {
            appDatabase.favoriteDao().getTracksId()
        }
        val updateFavorite = updateIsFavorite(
            createTracksFromJson(sharedPreferences.getString(VIEWED_TRACKS, null)),
            favoriteTrackIds)
        return updateFavorite
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

private const val LIMIT = 10
