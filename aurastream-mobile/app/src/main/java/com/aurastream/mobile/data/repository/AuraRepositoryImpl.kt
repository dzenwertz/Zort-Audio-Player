package com.aurastream.mobile.data.repository

import com.aurastream.mobile.data.dto.AddSongDto
import com.aurastream.mobile.data.dto.CreatePlaylistDto
import com.aurastream.mobile.data.dto.toDomain
import com.aurastream.mobile.data.remote.AuraApiService
import com.aurastream.mobile.data.remote.RetrofitClient
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.PlaylistItem
import com.aurastream.mobile.domain.model.Song
import com.aurastream.mobile.domain.repository.AuraRepository

class AuraRepositoryImpl(
    private val apiService: AuraApiService
) : AuraRepository {

    private val baseUrl: String
        get() = RetrofitClient.currentBaseUrl

    override suspend fun getRecommendedSongs(): Result<List<Song>> = runCatching {
        try {
            val response = apiService.getRecommendedSongs()
            if (response.isNotEmpty()) {
                response.map { it.toDomain(baseUrl) }
            } else {
                MockData.sampleSongs
            }
        } catch (e: Exception) {
            MockData.sampleSongs
        }
    }

    override suspend fun searchSongs(query: String?): Result<List<Song>> = runCatching {
        try {
            val response = apiService.searchSongs(query)
            if (response.isNotEmpty()) {
                response.map { it.toDomain(baseUrl) }
            } else if (query.isNullOrBlank()) {
                MockData.sampleSongs
            } else {
                MockData.sampleSongs.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.artistName.contains(query, ignoreCase = true) ||
                            it.genre.contains(query, ignoreCase = true)
                }
            }
        } catch (e: Exception) {
            if (query.isNullOrBlank()) {
                MockData.sampleSongs
            } else {
                MockData.sampleSongs.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.artistName.contains(query, ignoreCase = true)
                }
            }
        }
    }

    override suspend fun getSongById(id: Long): Result<Song> = runCatching {
        try {
            apiService.getSongById(id).toDomain(baseUrl)
        } catch (e: Exception) {
            MockData.sampleSongs.firstOrNull { it.id == id } ?: MockData.sampleSongs.first()
        }
    }

    override suspend fun getPlaylists(): Result<List<Playlist>> = runCatching {
        try {
            val response = apiService.getPlaylists()
            if (response.isNotEmpty()) {
                response.map { it.toDomain(baseUrl) }
            } else {
                MockData.samplePlaylists
            }
        } catch (e: Exception) {
            MockData.samplePlaylists
        }
    }

    override suspend fun getPlaylistById(id: Long): Result<Playlist> = runCatching {
        try {
            apiService.getPlaylistById(id).toDomain(baseUrl)
        } catch (e: Exception) {
            MockData.samplePlaylists.firstOrNull { it.id == id } ?: MockData.samplePlaylists.first()
        }
    }

    override suspend fun createPlaylist(name: String, description: String?): Result<Playlist> = runCatching {
        try {
            apiService.createPlaylist(CreatePlaylistDto(name, description)).toDomain(baseUrl)
        } catch (e: Exception) {
            val newPlaylist = Playlist(
                id = (MockData.samplePlaylists.size + 1).toLong(),
                name = name,
                description = description ?: "",
                coverUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?q=80&w=600&auto=format&fit=crop",
                createdAt = "2026-08-06",
                items = emptyList(),
                totalSongs = 0
            )
            MockData.samplePlaylists.add(newPlaylist)
            newPlaylist
        }
    }

    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long): Result<Playlist> = runCatching {
        try {
            apiService.addSongToPlaylist(playlistId, AddSongDto(songId)).toDomain(baseUrl)
        } catch (e: Exception) {
            val targetPlaylistIndex = MockData.samplePlaylists.indexOfFirst { it.id == playlistId }
            if (targetPlaylistIndex != -1) {
                val currentPlaylist = MockData.samplePlaylists[targetPlaylistIndex]
                val songToAdd = MockData.sampleSongs.firstOrNull { it.id == songId }
                if (songToAdd != null && !currentPlaylist.items.any { it.song.id == songId }) {
                    val newPlaylistItem = PlaylistItem(
                        id = (currentPlaylist.items.size + 1).toLong(),
                        song = songToAdd,
                        addedAt = "2026-08-06",
                        position = currentPlaylist.items.size + 1
                    )
                    val updatedItems = currentPlaylist.items + newPlaylistItem
                    val updatedPlaylist = currentPlaylist.copy(
                        items = updatedItems,
                        totalSongs = updatedItems.size
                    )
                    MockData.samplePlaylists[targetPlaylistIndex] = updatedPlaylist
                    updatedPlaylist
                } else {
                    currentPlaylist
                }
            } else {
                MockData.samplePlaylists.first()
            }
        }
    }

    override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long): Result<Playlist> = runCatching {
        try {
            apiService.removeSongFromPlaylist(playlistId, songId).toDomain(baseUrl)
        } catch (e: Exception) {
            val targetPlaylistIndex = MockData.samplePlaylists.indexOfFirst { it.id == playlistId }
            if (targetPlaylistIndex != -1) {
                val currentPlaylist = MockData.samplePlaylists[targetPlaylistIndex]
                val updatedItems = currentPlaylist.items.filterNot { it.song.id == songId }
                val updatedPlaylist = currentPlaylist.copy(
                    items = updatedItems,
                    totalSongs = updatedItems.size
                )
                MockData.samplePlaylists[targetPlaylistIndex] = updatedPlaylist
                updatedPlaylist
            } else {
                MockData.samplePlaylists.first()
            }
        }
    }

    override suspend fun deletePlaylist(playlistId: Long): Result<Unit> = runCatching {
        try {
            apiService.deletePlaylist(playlistId)
        } catch (e: Exception) {
            MockData.samplePlaylists.removeAll { it.id == playlistId }
        }
    }
}
