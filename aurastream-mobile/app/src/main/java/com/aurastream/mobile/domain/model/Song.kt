package com.aurastream.mobile.domain.model

data class Song(
    val id: Long,
    val title: String,
    val artistId: Long?,
    val artistName: String,
    val albumId: Long?,
    val albumTitle: String?,
    val coverUrl: String?,
    val durationSeconds: Int,
    val genre: String,
    val audioUrl: String,
    val playCount: Long,
    val bpm: Int,
    val isFavorite: Boolean = false
)
