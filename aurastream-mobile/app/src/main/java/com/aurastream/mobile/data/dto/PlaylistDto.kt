package com.aurastream.mobile.data.dto

import com.google.gson.annotations.SerializedName
import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.model.PlaylistItem

data class PlaylistItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("song") val song: SongDto,
    @SerializedName("addedAt") val addedAt: String?,
    @SerializedName("position") val position: Int
)

data class PlaylistDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("coverUrl") val coverUrl: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("items") val items: List<PlaylistItemDto>?,
    @SerializedName("totalSongs") val totalSongs: Int
)

fun PlaylistItemDto.toDomain(baseUrl: String): PlaylistItem = PlaylistItem(
    id = id,
    song = song.toDomain(baseUrl),
    addedAt = addedAt ?: "",
    position = position
)

fun PlaylistDto.toDomain(baseUrl: String): Playlist = Playlist(
    id = id,
    name = name,
    description = description,
    coverUrl = coverUrl ?: "https://images.unsplash.com/photo-1518609878373-06d740f60d8b",
    createdAt = createdAt,
    items = items?.map { it.toDomain(baseUrl) } ?: emptyList(),
    totalSongs = totalSongs
)
