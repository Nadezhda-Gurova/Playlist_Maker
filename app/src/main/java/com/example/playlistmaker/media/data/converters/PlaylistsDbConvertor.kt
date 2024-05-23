package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistsDbConvertor {
    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            imagePath = playlistEntity.imagePath,
            trackIds = convertJsonToTrackIds(playlistEntity.trackIds),
            trackCount = playlistEntity.trackCount,
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imagePath = playlist.imagePath,
            trackIds = convertTrackIdsToJson(playlist.trackIds),
            trackCount = playlist.trackCount,
        )
    }

    private fun convertTrackIdsToJson(trackIds: List<Int>): String {
        return Gson().toJson(trackIds)
    }

    private fun convertJsonToTrackIds(trackIds: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(trackIds, listType)
    }
}
