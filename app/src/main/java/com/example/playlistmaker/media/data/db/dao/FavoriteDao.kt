package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteDao {
    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_table")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_table")
    suspend fun getTracksId(): List<Int>

}