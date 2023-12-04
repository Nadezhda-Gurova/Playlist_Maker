package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.dto.ITunesResponse

class TrackMapper {
    fun map(response: ITunesResponse): List<Track> {
        return response.results.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTime = it.trackTime.time,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl,
            )
        }
    }

}