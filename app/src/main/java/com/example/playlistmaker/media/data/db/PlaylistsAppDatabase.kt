package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity


@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistsAppDatabase: RoomDatabase() {
    abstract fun playlistsDao(): PlaylistDao
}