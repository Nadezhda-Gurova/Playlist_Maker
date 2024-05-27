package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.converters.PlaylistsDbConvertor
import com.example.playlistmaker.media.data.db.PlaylistsAppDatabase
import com.example.playlistmaker.media.data.db.PlaylistsTracksDatabase
import com.example.playlistmaker.media.data.converters.PlaylistsTracksDbConverter
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.domain.repository.PlaylistMakerRepository
import com.example.playlistmaker.media.ui.playlist.recyclerview.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistMakerRepositoryImpl(
    private val appDatabase: PlaylistsAppDatabase,
    private val playlistsTracksDatabase: PlaylistsTracksDatabase,
    private val playlistsDbConvertor: PlaylistsDbConvertor,
    private val playlistsTracksDbConverter: PlaylistsTracksDbConverter
) : PlaylistMakerRepository {

    override val state = MutableStateFlow<List<Playlist>>(emptyList())

    override suspend fun insertPlaylist(playlist: Playlist) {
        val entity = convertFromPlaylist(playlist)
        val curTime = System.currentTimeMillis()
        appDatabase.playlistsDao()
            .insert(entity.copy(timestamp = curTime))
        state.emit(state.value + listOf(playlist))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val ids = playlist.trackIds.toMutableList().apply { add(track.trackId) }
        val count = ids.size
        val playlist = playlist.copy(trackIds = ids, trackCount = count)

        playlistsTracksDatabase.playlistsTracksDao()
            .insertTrack(convertFromTrackToPlaylistTrackEntity(track))
        updatePlaylist(playlist)
    }


    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().update(convertFromPlaylist(playlist))
        val currentLists = state.value.toMutableList()
        val index = currentLists.indexOfFirst { it.id == playlist.id }
        if (index != -1) {
            currentLists[index] = playlist
        }
        state.emit(currentLists)
    }

    override suspend fun getAllPlaylists() {
        val playlists = appDatabase.playlistsDao().getAllPlaylists()
        val sortedPlaylists = playlists.sortedByDescending { it.timestamp }
        state.emit(convertFromPlaylistsEntity(sortedPlaylists))
    }

    override suspend fun invalidateState() {
        state.emit(emptyList())
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistsDbConvertor.map(appDatabase.playlistsDao().getPlaylistById(playlistId))
    }

//    override suspend fun getAllTracks(): List<Track> {
//        val allTracksEntities = playlistsTracksDatabase.playlistsTracksDao().getTracksById()
//        return allTracksEntities.map { playlistsTracksDbConverter.map(it) }
//    }
    override suspend fun getTracksByIds(trackIds: List<Int>): List<Track> {
        val trackEntities = playlistsTracksDatabase.playlistsTracksDao().getTracksById(trackIds)
        return trackEntities.map { playlistsTracksDbConverter.map(it) }
    }

    override suspend fun deletePlaylistById(playlistId: Int) {
        appDatabase.playlistsDao().deletePlaylistById(playlistId)
    }


    private fun convertFromTrackToPlaylistTrackEntity(track: Track): PlaylistTrackEntity {
        return playlistsTracksDbConverter.map(track)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
//        playlist.trackIds.toMutableList().remove(track.trackId)
//        playlist.trackCount--
//        appDatabase.playlistsDao().update(convertFromPlaylist(playlist))
//        playlistsTracksDatabase.playlistsTracksDao()
//            .deleteTrack(track.trackId)
    }

    private fun convertFromPlaylistsEntity(playlistEntities: List<PlaylistEntity>): List<Playlist> {
        return playlistEntities.map { entity -> playlistsDbConvertor.map(entity) }
    }

    private fun convertFromPlaylist(playlistEntities: Playlist): PlaylistEntity {
        return playlistsDbConvertor.map(playlistEntities)
    }
}
