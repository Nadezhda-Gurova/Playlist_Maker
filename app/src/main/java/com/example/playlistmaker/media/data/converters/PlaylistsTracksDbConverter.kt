package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.search.domain.models.Track

class PlaylistsTracksDbConverter {
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            timestamp = track.timestamp,
        )
    }

    fun map(playlistTrackEntity: PlaylistTrackEntity): Track {
        return Track(
            trackId = playlistTrackEntity.trackId,
            trackName = playlistTrackEntity.trackName,
            artistName = playlistTrackEntity.artistName,
            trackTime = playlistTrackEntity.trackTime,
            artworkUrl100 = playlistTrackEntity.artworkUrl100,
            collectionName = playlistTrackEntity.collectionName,
            releaseDate = playlistTrackEntity.releaseDate,
            primaryGenreName = playlistTrackEntity.primaryGenreName,
            country = playlistTrackEntity.country,
            previewUrl = playlistTrackEntity.previewUrl,
            timestamp = playlistTrackEntity.timestamp,
        )
    }
}