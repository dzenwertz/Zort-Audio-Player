package com.aurastream.mobile.data.repository

import com.aurastream.mobile.data.dto.AddSongDto
import com.aurastream.mobile.data.dto.CreatePlaylistDto
import com.aurastream.mobile.data.dto.toDomain
import com.aurastream.mobile.data.remote.AuraApiService
import com.aurastream.mobile.data.remote.RetrofitClient
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.domain.repository.AuraRepository

class AuraRepositoryImpl(
    private val apiService: AuraApiService
) : AuraRepository {

    private val baseUrl: String
        get() = RetrofitClient.currentBaseUrl

    override suspend fun getRecommendedSongs(): Result<List<Song>> = runCatching {
        apiService.getRecommendedSongs().map { it.toDomain(baseUrl) }
    }

    override suspend fun searchSongs(query: String?): Result<List<Song>> = runCatching {
        apiService.searchSongs(query).map { it.toDomain(baseUrl) }
    }

    override suspend fun getSongById(id: Long): Result<Song> = runCatching {
        apiService.getSongById(id).toDomain(baseUrl)
    }

    override suspend fun getPlaylists(): Result<List<Playlist>> = runCatching {
        apiService.getPlaylists().map { it.toDomain(baseUrl) }
    }

    override suspend fun getPlaylistById(id: Long): Result<Playlist> = runCatching {
        apiService.getPlaylistById(id).toDomain(baseUrl)
    }

    override suspend fun createPlaylist(name: String, description: String?): Result<Playlist> = runCatching {
        apiService.createPlaylist(CreatePlaylistDto(name, description)).toDomain(baseUrl)
    }

    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long): Result<Playlist> = runCatching {
        apiService.addSongToPlaylist(playlistId, AddSongDto(songId)).toDomain(baseUrl)
    }

    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long): Result<Playlist> = runCatching {
        apiService.removeSongFromPlaylist(playlistId, songId).toDomain(baseUrl)
    }

    override suspend fun deletePlaylist(playlistId: Long): Result<Unit> = runCatching {
        apiService.deletePlaylist(playlistId)
    }
}
