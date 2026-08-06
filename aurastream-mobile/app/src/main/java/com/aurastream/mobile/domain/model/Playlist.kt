package com.aurastream.mobile.domain.model

data class PlaylistItem(
    val id: Long,
    val song: Song,
    val addedAt: String,
    val position: Int
)

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val coverUrl: String?,
    val createdAt: String?,
    val items: List<PlaylistItem>,
    val totalSongs: Int
)
