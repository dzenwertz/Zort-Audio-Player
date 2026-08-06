package com.aurastream.mobile.data.dto

import com.google.gson.annotations.SerializedName
import com.aurastream.mobile.domain.model.Song

data class SongDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("artistId") val artistId: Long?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("albumId") val albumId: Long?,
    @SerializedName("albumTitle") val albumTitle: String?,
    @SerializedName("coverUrl") val coverUrl: String?,
    @SerializedName("durationSeconds") val durationSeconds: Int,
    @SerializedName("genre") val genre: String?,
    @SerializedName("audioUrl") val audioUrl: String?,
    @SerializedName("playCount") val playCount: Long,
    @SerializedName("bpm") val bpm: Int
)

fun SongDto.toDomain(baseUrl: String): Song {
    val fullAudioUrl = if (audioUrl != null && audioUrl.startsWith("http")) {
        audioUrl
    } else {
        "${baseUrl.trimEnd('/')}${audioUrl ?: "/api/v1/songs/$id/stream"}"
    }

    return Song(
        id = id,
        title = title,
        artistId = artistId,
        artistName = artistName ?: "Artista Desconocido",
        albumId = albumId,
        albumTitle = albumTitle,
        coverUrl = coverUrl ?: "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4",
        durationSeconds = durationSeconds,
        genre = genre ?: "Pop",
        audioUrl = fullAudioUrl,
        playCount = playCount,
        bpm = bpm
    )
}
