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

//    fun map(playlist: PlaylistTrackEntity): Track {
//        return Track(
//            trackId = playlist.trackId,
//            trackName = playlist.trackName,
//            artistName = playlist.artistName,
//            trackTime = playlist.trackTime,
//            artworkUrl100 = playlist.artworkUrl100,
//            collectionName = playlist.collectionName,
//            releaseDate = playlist.releaseDate,
//            primaryGenreName = playlist.primaryGenreName,
//            country = playlist.country,
//            previewUrl = playlist.previewUrl,
//            timestamp = playlist.timestamp,
//        )
//    }
}