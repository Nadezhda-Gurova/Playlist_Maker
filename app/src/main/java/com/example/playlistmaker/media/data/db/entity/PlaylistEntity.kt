package com.example.playlistmaker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var trackIds: String, // This will store the JSON string of track IDs
    var trackCount: Int,
    val timestamp: Long = 0L,
) {
//    fun getTrackIds(): List<Int> {
//        val listType = object : TypeToken<List<Int>>() {}.type
//        return Gson().fromJson(trackIds, listType)
//    }
//
//    fun setTrackIds(ids: List<Int>) {
//        trackIds = Gson().toJson(ids)
//    }
}