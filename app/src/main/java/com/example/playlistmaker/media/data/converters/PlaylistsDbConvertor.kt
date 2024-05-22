package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist

class PlaylistsDbConvertor {
    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            name = playlistEntity.name,
            description = playlistEntity.description,
            imagePath = playlistEntity.imagePath,
            trackIds = playlistEntity.trackIds,
            trackCount = playlistEntity.trackCount,
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            name = playlist.name,
            description = playlist.description,
            imagePath = playlist.imagePath,
            trackIds = playlist.trackIds,
            trackCount = playlist.trackCount,
        )
    }
}
