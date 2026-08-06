package com.aurastream.mobile.data.remote

import com.aurastream.mobile.data.dto.*
import retrofit2.http.*

interface AuraApiService {

    @GET("api/v1/songs")
    suspend fun getAllSongs(): List<SongDto>

    @GET("api/v1/songs/{id}")
    suspend fun getSongById(@Path("id") id: Long): SongDto

    @GET("api/v1/songs/search")
    suspend fun searchSongs(@Query("query") query: String? = null): List<SongDto>

    @GET("api/v1/songs/recommended")
    suspend fun getRecommendedSongs(): List<SongDto>

    @POST("api/v1/songs/{id}/play")
    suspend fun incrementPlayCount(@Path("id") id: Long): SongDto

    @GET("api/v1/playlists")
    suspend fun getPlaylists(): List<PlaylistDto>

    @GET("api/v1/playlists/{id}")
    suspend fun getPlaylistById(@Path("id") id: Long): PlaylistDto

    @POST("api/v1/playlists")
    suspend fun createPlaylist(@Body request: CreatePlaylistDto): PlaylistDto

    @PUT("api/v1/playlists/{id}")
    suspend fun updatePlaylist(
        @Path("id") id: Long,
        @Body request: UpdatePlaylistDto
    ): PlaylistDto

    @POST("api/v1/playlists/{id}/songs")
    suspend fun addSongToPlaylist(
        @Path("id") id: Long,
        @Body request: AddSongDto
    ): PlaylistDto

    @DELETE("api/v1/playlists/{id}/songs/{songId}")
    suspend fun removeSongFromPlaylist(
        @Path("id") id: Long,
        @Path("songId") songId: Long
    ): PlaylistDto

    @DELETE("api/v1/playlists/{id}")
    suspend fun deletePlaylist(@Path("id") id: Long)
}
