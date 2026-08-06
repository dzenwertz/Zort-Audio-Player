package com.aurastream.mobile.domain.repository

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song

interface AuraRepository {
    suspend fun getRecommendedSongs(): Result<List<Song>>
    suspend fun searchSongs(query: String?): Result<List<Song>>
    suspend fun getSongById(id: Long): Result<Song>
    suspend fun getPlaylists(): Result<List<Playlist>>
    suspend fun getPlaylistById(id: Long): Result<Playlist>
    suspend fun createPlaylist(name: String, description: String?): Result<Playlist>
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long): Result<Playlist>
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long): Result<Playlist>
    suspend fun deletePlaylist(playlistId: Long): Result<Unit>
}
