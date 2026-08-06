package com.aurastream.mobile.domain.usecase

import com.aurastream.mobile.domain.model.Playlist
import com.aurastream.mobile.domain.repository.AuraRepository

class RemoveSongFromPlaylistUseCase(private val repository: AuraRepository) {
    suspend operator fun invoke(playlistId: Long, songId: Long): Result<Playlist> {
        return repository.removeSongFromPlaylist(playlistId, songId)
    }
}
