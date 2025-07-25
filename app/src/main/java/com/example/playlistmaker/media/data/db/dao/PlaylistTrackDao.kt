package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (:trackIds)")
    suspend fun getTracksById(trackIds: List<Int>): List<PlaylistTrackEntity>

    @Query("""
        DELETE FROM playlist_tracks
        WHERE trackId NOT IN (
            SELECT trackId FROM playlist_tracks
        )
    """)
    suspend fun deleteOrphanedTracks()
}
