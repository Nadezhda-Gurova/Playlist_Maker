package com.example.playlistmaker.media.data.converters

import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.google.gson.JsonSyntaxException
import org.junit.Assert.*

import org.junit.Test

class PlaylistsDbConvertorTest {
    
    @Test
    fun `wrong tracks id format `() {
        val playlistsTracksDbConverter = PlaylistsDbConvertor()

        val playlistEntity = PlaylistEntity(
            name = "name",
            description = "description",
            imagePath = "imagePath",
            trackIds = "1234,1235",
            trackCount = 1,
        )

        try {
            playlistsTracksDbConverter.map(playlistEntity)
        } catch (e: Exception) {
            assert(e is JsonSyntaxException)
        }
    }

    @Test
    fun `proper ids format`() {
        val playlistsTracksDbConverter = PlaylistsDbConvertor()

        val playlistEntity = PlaylistEntity(
            name = "name",
            description = "description",
            imagePath = "imagePath",
            trackIds = "[1234,1235]",
            trackCount = 1,
        )

        val res = playlistsTracksDbConverter.map(playlistEntity)
        assert(res.trackIds.size == 2)

    }
    
}