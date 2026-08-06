package com.aurastream.mobile.data.dto

import com.google.gson.annotations.SerializedName

data class CreatePlaylistDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?
)

data class UpdatePlaylistDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?
)

data class AddSongDto(
    @SerializedName("songId") val songId: Long
)
